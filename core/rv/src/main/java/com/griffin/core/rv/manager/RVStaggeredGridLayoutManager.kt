package com.griffin.core.rv.manager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 作用类描述：自定义 StaggeredGridLayoutManager，设置是否允许滑动
 */
class RVStaggeredGridLayoutManager : StaggeredGridLayoutManager {
    private var scrollEnabled = true

    constructor(spanCount: Int, @RecyclerView.Orientation orientation: Int) : super(
        spanCount,
        orientation
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    fun setScrollEnabled(enabled: Boolean): RVStaggeredGridLayoutManager {
        scrollEnabled = enabled
        return this
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && scrollEnabled
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && scrollEnabled
    }
}