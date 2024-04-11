package com.griffin.core.router

/**
 * 路由路径
 */
object RoutePath {

    object Main {
        const val MAIN = "/app/MainActivity"
    }

    object Login {
        const val LOGIN = "/login/LoginActivity"
    }

    object Type {
        const val TYPE = "/type/TypeActivity"
    }

    object Account {
        const val ACCOUNT = "/account/AccountActivity"
    }

    object Map {
        const val MAP = "/map/MapActivity"
        const val MAP_DETAIL = "/map/MapDetailActivity"
    }

    object Add {

        /**
         * type 0：新增 1：修改 2：查看
         * id 账单ID
         */
        const val BILL = "/add/AddActivity"
    }
}