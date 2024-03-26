package com.griffin.core.rv.model

/**
 * 选择密封类
 */
sealed class SelectSealed {

    /**
     * 什么都不做
     */
    data object None : SelectSealed()

    /**
     * 单选模式密封类
     */
    data object Single : SelectSealed()

    /**
     * 多选模式密封类
     */
    data object Multiple : SelectSealed()
}
