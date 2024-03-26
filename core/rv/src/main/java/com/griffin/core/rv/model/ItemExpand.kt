package com.griffin.core.rv.model

interface ItemExpand {

    // 同级别分组的索引位置
    var itemGroupPosition: Int

    /** 是否需要展开分组 */
    var itemExpand: Boolean

    /** 子列表 */
    var itemChildList: List<Any?>?
}