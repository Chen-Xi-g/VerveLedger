package com.griffin.core.utils.mmkv

/**
 * 持久化数据
 */
object BaseMV {

    object System: Delegates(){
        override fun zoneId(): String {
            return "system"
        }

        /**
         * 是否第一次启动
         */
        var isFirst by boolean(true)

    }

    object User: Delegates(){
        override fun zoneId(): String {
            return "user"
        }

        /**
         * Token
         */
        var token by string()
    }

}