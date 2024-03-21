package com.griffin.core.utils.mmkv

/**
 * 持久化数据
 */
object BaseMV {

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