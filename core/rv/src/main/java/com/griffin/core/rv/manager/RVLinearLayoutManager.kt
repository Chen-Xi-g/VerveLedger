package com.griffin.core.rv.manager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 自定义LayoutLinearManager，设置是否允许滑动
 */
class RVLinearLayoutManager : LinearLayoutManager {

    private var scrollEnabled = true

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean
    ) : super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    fun setScrollEnabled(enabled: Boolean): RVLinearLayoutManager {
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