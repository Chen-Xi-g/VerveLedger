package com.griffin.feature.map.component

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.OnMyLocationChangeListener
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.data.model.MapLocationModel
import com.griffin.core.dialog.TipDialog
import com.griffin.core.router.RoutePath
import com.griffin.core.rv.linear
import com.griffin.core.rv.setData
import com.griffin.core.rv.setup
import com.griffin.core.utils.gone
import com.griffin.core.utils.isDarkTheme
import com.griffin.core.utils.isVisible
import com.griffin.core.utils.visible
import com.griffin.feature.map.R
import com.griffin.feature.map.databinding.ActivityMapBinding
import com.griffin.feature.map.databinding.AdapterSearchBinding
import com.therouter.router.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@Route(path = RoutePath.Map.MAP)
class MapActivity : HiltBaseActivity<ActivityMapBinding>(R.layout.activity_map) {

    override val viewModel: MapViewModel by viewModels()

    private lateinit var searchView: SearchView
    private lateinit var aMap: AMap
    private lateinit var uiSetting: UiSettings
    private var selectMarker: Marker? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            // 权限已授予
            onMapReady()
        } else {
            // 权限未授予
            tipDialog()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        rootBinding.baseTitleLayout.root.gone()
        binding.toolbar.title = "选择位置"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.mapView.onCreate(savedInstanceState)
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                onMapReady()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                tipDialog()
            }

            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
        initAdapter()
    }

    override fun obtainData() {
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.tipList.collectLatest {
                if (it.isNotEmpty()) {
                    binding.recyclerView.setData(it)
                    binding.recyclerView.visible()
                } else {
                    binding.recyclerView.setData(emptyList<Tip>())
                    binding.recyclerView.gone()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.tip.collectLatest {
                binding.item = it
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerView.linear()
            .setup {
                addType<Tip>(R.layout.adapter_search)
                onBind {
                    when (val type = getType()) {
                        is Tip -> {
                            getBinding<AdapterSearchBinding>()?.item = type
                        }
                    }
                }

                onItemClick {
                    when (val type = getType()) {
                        is Tip -> {
                            val latLng = LatLng(type.point.latitude, type.point.longitude)
                            selectMarker?.remove()
                            // 绘制Marker
                            selectMarker = aMap.addMarker(MarkerOptions().position(latLng))
                            // 移动地图
                            aMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition(
                                        latLng,
                                        aMap.cameraPosition.zoom,
                                        aMap.cameraPosition.tilt,
                                        aMap.cameraPosition.bearing
                                    )
                                )
                            )
                            closeSearchView()
                            showBottomSheet(type)
                        }
                    }
                }
            }
    }

    /**
     * 加载地图
     */
    private fun onMapReady() {
        showContent()
        aMap = binding.mapView.map
        uiSetting = aMap.uiSettings
        aMap.mapType = if (isDarkTheme()) {
            AMap.MAP_TYPE_NIGHT
        } else {
            AMap.MAP_TYPE_NORMAL
        }
        initLocalStyle()
        initMapUI()
        initBottomSheet()
    }

    /**
     * 初始化BottomSheet
     */
    private fun initBottomSheet() {
        hideBottomSheet()
        binding.ivClose.setOnClickListener {
            hideBottomSheet()
        }
        binding.tvConfirm.setOnClickListener {
            // 确认选择
            val intent = Intent()
            intent.putExtra(
                "data", MapLocationModel(
                    address = (binding.item?.district ?: "") + " - " + (binding.item?.address
                        ?: "") + " - " + (binding.item?.name ?: ""),
                    longitude = binding.item?.point?.longitude ?: 0.0,
                    latitude = binding.item?.point?.longitude ?: 0.0
                )
            )
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    /**
     * 显示BottomSheet
     */
    private fun showBottomSheet(tip: Tip) {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        viewModel.updateCurrentTip(tip)
    }

    /**
     * 隐藏BottomSheet
     */
    private fun hideBottomSheet() {
        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        viewModel.updateCurrentTip(Tip())
        selectMarker?.remove()
    }

    /**
     * 初始化定位样式
     */
    private fun initLocalStyle() {
        val localStyle = MyLocationStyle()
        localStyle.interval(2000)
        localStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        aMap.myLocationStyle = localStyle
        aMap.isMyLocationEnabled = true
        aMap.setOnMyLocationChangeListener(object : OnMyLocationChangeListener {
            override fun onMyLocationChange(location: Location) {
                aMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(location.latitude, location.longitude),
                            if (aMap.cameraPosition.zoom < 17.0f) 17.0f else aMap.cameraPosition.zoom,
                            aMap.cameraPosition.tilt,
                            aMap.cameraPosition.bearing
                        )
                    )
                )
                aMap.removeOnMyLocationChangeListener(this)
            }
        })
    }

    /**
     * 初始化地图UI
     */
    private fun initMapUI() {
        aMap.moveCamera(
            CameraUpdateFactory.zoomTo(17f)
        )
        uiSetting.isZoomControlsEnabled = true
        uiSetting.isMyLocationButtonEnabled = true
    }

    /**
     * 提示权限对话框
     */
    private fun tipDialog() {
        TipDialog(
            this,
            message = getString(R.string.map_permission_needed)
        ).show()
    }

    /**
     * 关闭搜索框
     */
    private fun closeSearchView() {
        binding.toolbar.collapseActionView()
        searchView.setQuery("", false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_menu_options, menu)

        val searchActionView = menu.findItem(R.id.action_search)
        searchActionView.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                return true
            }
        })

        searchView = searchActionView.actionView as SearchView
        searchView.queryHint = getString(R.string.map_search_address_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val inputQuery = InputtipsQuery(query, "")
                val inputTIps = Inputtips(this@MapActivity, inputQuery)
                inputTIps.setInputtipsListener(viewModel.inputListener)
                inputTIps.requestInputtipsAsyn()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val inputQuery = InputtipsQuery(newText, "")
                val inputTIps = Inputtips(this@MapActivity, inputQuery)
                inputTIps.setInputtipsListener(viewModel.inputListener)
                inputTIps.requestInputtipsAsyn()
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun paddingWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // 设置标题栏顶部padding
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}