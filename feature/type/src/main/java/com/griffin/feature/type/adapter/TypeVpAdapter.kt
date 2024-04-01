package com.griffin.feature.type.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.griffin.feature.type.component.TypeFragment

class TypeVpAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = TypeFragment.newInstance(position)

    /**
     * 获取指定位置的Fragment
     */
    fun getFragment(position: Int): TypeFragment {
        return fragmentManager.findFragmentByTag("f$position") as TypeFragment
    }
}