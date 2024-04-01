package com.griffin.core.rv.manager

import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State

/**
 * 流式布局
 */
class RVFlowLayoutManager : RecyclerView.LayoutManager() {

    private var scrollEnabled = true

    var mWidth: Int = 0
        private set
    var mHeight: Int = 0
        private set

    //计算显示的内容的高度
    var totalHeight = 0
        private set

    private var left = 0
    private var top: Int = 0
    private var right: Int = 0

    //最大容器的宽度
    private var usedMaxWidth = 0

    //竖直方向上的偏移量
    private var verticalScrollOffset = 0
    private var row: Row = Row()
    private val lineRows: MutableList<Row> = mutableListOf()

    //保存所有的Item的上下左右的偏移量信息
    private val allItemFrames = SparseArray<Rect>()

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: State) {
        totalHeight = 0
        var cuLineTop = top
        // 当前行使用的宽度
        var cuLineWidth = 0
        var itemLeft: Int
        var itemTop: Int
        var maxHeightItem = 0
        row = Row()
        lineRows.clear()
        allItemFrames.clear()
        removeAllViews()
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            verticalScrollOffset = 0
            return
        }
        if (childCount == 0 && state.isPreLayout) {
            return
        }
        detachAndScrapAttachedViews(recycler)
        if (childCount == 0) {
            mWidth = getWidth()
            mHeight = getHeight()
            left = paddingLeft
            right = paddingRight
            top = paddingTop
            usedMaxWidth = mWidth - left - right
        }

        for (i in 0 until itemCount) {
            // 不换行
            val childAt = recycler.getViewForPosition(i)
            if (View.GONE == childAt.visibility) {
                continue
            }
            measureChildWithMargins(childAt, 0, 0)
            val childWidth = getDecoratedMeasuredWidth(childAt)
            val childHeight = getDecoratedMeasuredHeight(childAt)
            if (cuLineWidth + childWidth <= usedMaxWidth) {
                itemLeft = left + cuLineWidth
                itemTop = cuLineTop
                val frame = allItemFrames.get(i) ?: Rect()
                frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight)
                allItemFrames.put(i, frame)
                cuLineWidth += childWidth
                maxHeightItem = Math.max(maxHeightItem, childHeight)
                row.views.add(Item(childHeight, childAt, frame))
                row.cuTop = cuLineTop
                row.maxHeight = maxHeightItem
            } else {
                // 换行
                formatAboveRow()
                cuLineTop += maxHeightItem
                totalHeight += maxHeightItem
                itemTop = cuLineTop
                itemLeft = left
                val frame = allItemFrames[i] ?: Rect()
                frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight)
                allItemFrames.put(i, frame)
                cuLineWidth = childWidth
                maxHeightItem = childHeight
                row.views.add(Item(childHeight, childAt, frame))
                row.cuTop = cuLineTop
                row.maxHeight = maxHeightItem
            }
            // 最后一行刷新布局
            if (i == itemCount - 1) {
                formatAboveRow()
                totalHeight += maxHeightItem
            }
        }
        totalHeight = Math.max(totalHeight, getVerticalSpace())
        fillLayout(state)
    }

    private fun fillLayout(state: State) {
        if (state.isPreLayout || itemCount == 0) return
        lineRows.forEach {
            it.views.forEachIndexed { index, item ->
                measureChildWithMargins(item.view, 0, 0)
                addView(item.view)
                layoutDecoratedWithMargins(
                    item.view,
                    item.rect.left,
                    item.rect.top - verticalScrollOffset,
                    item.rect.right,
                    item.rect.bottom - verticalScrollOffset
                )
            }
        }
    }

    /**
     * 计算每一行没有居中的ViewGroup，然后进行居中处理
     */
    private fun formatAboveRow() {
        val views = row.views
        views.forEachIndexed { index, item ->
            val position = getPosition(item.view)
            // 如果这个Item的位置不在该行中间位置的话，就需要进行居中处理
            if (allItemFrames.get(position).top < row.cuTop + (row.maxHeight - views[index].useHeight) / 2) {
                val frame = allItemFrames.get(position) ?: Rect()
                frame.set(
                    allItemFrames.get(position).left,
                    row.cuTop + (row.maxHeight - views[index].useHeight) / 2,
                    allItemFrames.get(position).right,
                    row.cuTop + (row.maxHeight - views[index].useHeight) / 2 + getDecoratedMeasuredHeight(
                        item.view
                    )
                )
                allItemFrames.put(position, frame)
                item.rect = frame
                views[index] = item
            }
        }
        row.views = views
        lineRows.add(row)
        row = Row()
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && scrollEnabled
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: State): Int {
        val travel = if (verticalScrollOffset + dy < 0) {
            -verticalScrollOffset
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
            totalHeight - getVerticalSpace() - verticalScrollOffset
        } else {
            dy
        }

        verticalScrollOffset += travel

        offsetChildrenVertical(-travel)
        fillLayout(state)
        return travel
    }

    private fun getVerticalSpace(): Int {
        return getHeight() - paddingBottom - paddingTop
    }

    fun setScrollEnabled(enabled: Boolean): RVFlowLayoutManager {
        scrollEnabled = enabled
        return this
    }

    /**
     * Item信息
     *
     * @property useHeight 使用的高度
     * @property view View
     * @property rect Rect
     */
    data class Item(
        var useHeight: Int,
        val view: View,
        var rect: Rect
    )

    /**
     * 行信息
     *
     * @property cuTop 每一行的头部左边
     * @property maxHeight 每一行需要占据的最大高度
     * @property views 每一行存储的item
     */
    internal data class Row(
        var cuTop: Int = 0,
        var maxHeight: Int = 0,
        var views: MutableList<Item> = mutableListOf()
    )
}