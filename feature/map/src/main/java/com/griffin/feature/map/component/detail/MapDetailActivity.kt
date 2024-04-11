package com.griffin.feature.map.component.detail

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.data.model.BillListModel
import com.griffin.core.dialog.TipDialog
import com.griffin.core.router.RoutePath
import com.griffin.core.rv.linear
import com.griffin.core.rv.setData
import com.griffin.core.rv.setup
import com.griffin.core.utils.isDarkTheme
import com.griffin.core.utils.isVisible
import com.griffin.core.utils.layoutToBitmap
import com.griffin.feature.map.R
import com.griffin.feature.map.databinding.ActivityMapDetailBinding
import com.griffin.feature.map.databinding.AdapterListDetailBinding
import com.therouter.router.Autowired
import com.therouter.router.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.jessyan.autosize.utils.AutoSizeUtils

@AndroidEntryPoint
@Route(path = RoutePath.Map.MAP_DETAIL)
class MapDetailActivity : HiltBaseActivity<ActivityMapDetailBinding>(R.layout.activity_map_detail) {

    override val viewModel: MapDetailViewModel by viewModels()

    companion object {

        /**
         * 单个账单
         */
        const val TYPE_DETAIL = 1

        /**
         * 地图账单
         */
        const val TYPE_MAP = 2

        /**
         * 参数
         *
         * @param type 类型 [TYPE_DETAIL] [TYPE_MAP]
         * @param id 账单id
         */
        fun params(type: Int = TYPE_DETAIL, id: Long = 0): Bundle {
            return Bundle().apply {
                putInt("type", type)
                putLong("id", id)
            }
        }
    }

    /**
     * 类型
     *
     * [TYPE_DETAIL] [TYPE_MAP]
     */
    @Autowired
    @JvmField
    var type: Int = 0

    /**
     * 账单id
     */
    @Autowired
    @JvmField
    var id: Long = 0

    // 是否第一次加载
    private var isFirst = true

    private lateinit var aMap: AMap
    private lateinit var uiSetting: UiSettings
    private var markers: MutableList<Marker> = mutableListOf()

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
        setTitleName("账单地图")
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
        binding.ivMenu.setOnClickListener {
            toggleView()
        }
    }

    private fun toggleView() {
        if (binding.rvList.isVisible()) {
            binding.rvList.hideAnim()
        } else {
            binding.rvList.showAnim()
        }
    }

    override fun obtainData() {
        when (this.type) {
            TYPE_MAP -> {
                initAdapter()
                viewModel.loadList()
            }

            TYPE_DETAIL -> {
                viewModel.loadDetail(id)
            }
        }
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.billList.collectLatest {
                addMarkers(it)
            }
        }
        lifecycleScope.launch {
            viewModel.billDetail.collectLatest {
                addMarkers(listOf(it))
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
        aMap.setOnMarkerClickListener {
            showBottomSheet(it)
            return@setOnMarkerClickListener true
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
    }

    /**
     * 给地图添加marker
     */
    private fun addMarkers(list: List<BillListModel>) {
        lifecycleScope.launch {
            list.forEachIndexed { index, billListModel ->
                val marker =
                    aMap.addMarker(
                        markerOption(billListModel)
                    )
                marker.`object` = billListModel
                marker.isClickable = true
                if (index == 0 && this@MapDetailActivity.type == TYPE_DETAIL) {
                    showBottomSheet(marker)
                }
                markers.add(marker)
            }
            binding.rvList.setData(markers)
        }
    }

    /**
     * 显示BottomSheet
     */
    private fun showBottomSheet(marker: Marker) {
        val model = marker.`object`
        if (model is BillListModel) {
            binding.detail = model
            moveMapCamera(LatLng(model.latitude, model.longitude))
        }
        lifecycleScope.launch {
            delay(500)
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        isFirst = false
    }

    /**
     * 隐藏BottomSheet
     */
    private fun hideBottomSheet() {
        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * 初始化地图UI
     */
    private fun initMapUI() {
        uiSetting.isZoomControlsEnabled = true
        uiSetting.isMyLocationButtonEnabled = true
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
        aMap.setOnMyLocationChangeListener(object : AMap.OnMyLocationChangeListener {
            override fun onMyLocationChange(location: Location) {
                if (this@MapDetailActivity.type == TYPE_DETAIL) {
                    aMap.moveCamera(
                        CameraUpdateFactory.zoomTo(15f)
                    )
                } else {
                    moveMapCamera(LatLng(location.latitude, location.longitude))
                }
                aMap.removeOnMyLocationChangeListener(this)
            }
        })
    }

    private fun moveMapCamera(latLng: LatLng) {
        aMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    latLng,
                    if (aMap.cameraPosition.zoom < 10.0f) 10.0f else aMap.cameraPosition.zoom,
                    aMap.cameraPosition.tilt,
                    aMap.cameraPosition.bearing
                )
            )
        )
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        binding.rvList.linear()
            .setup {
                addType<Marker>(R.layout.adapter_list_detail)
                onBind {
                    when (val type = getType()) {
                        is Marker -> {
                            (type.`object` as? BillListModel)?.let {
                                getBinding<AdapterListDetailBinding>()?.item = it
                            }
                        }
                    }
                }
                onItemClick {
                    when (val type = getType()) {
                        is Marker -> {
                            (type.`object` as? BillListModel)?.let {
                                toggleView()
                                moveMapCamera(type.position)
                            }
                        }
                    }
                }
            }
    }

    /**
     * 获取Marker配置
     *
     * @param model 账单详情数据
     */
    private fun markerOption(model: BillListModel): MarkerOptions {
        return MarkerOptions()
            .icon(markerBitmap(model.billName))
            .position(
                LatLng(
                    model.latitude,
                    model.longitude
                )
            )
    }

    /**
     * 设置Marker的Bitmap资源
     *
     * @param content Marker 内容
     */
    private fun markerBitmap(content: String): BitmapDescriptor {
        val layoutToBitmap = layoutToBitmap(R.layout.map_marker) {
            it.findViewById<AppCompatTextView>(R.id.tv_content)?.text = content
        }

        // 将 Bitmap 转换为 BitmapDescriptor
        return BitmapDescriptorFactory.fromBitmap(layoutToBitmap)
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

    override fun paddingWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(rootBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            // 设置标题栏顶部padding
            rootBinding.baseTitleLayout.root.setPadding(0, systemBars.top, 0, 0)
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