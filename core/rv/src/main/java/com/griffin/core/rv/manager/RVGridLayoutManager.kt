package com.griffin.core.rv.manager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 自定义GridLayoutManager，设置是否允许滑动
 */
class RVGridLayoutManager : GridLayoutManager {
    private var scrollEnabled = true

    constructor(context: Context, spanCount: Int) : super(context, spanCount)

    constructor(
        context: Context,
        spanCount: Int,
        @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    fun setScrollEnabled(enabled: Boolean): RVGridLayoutManager {
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