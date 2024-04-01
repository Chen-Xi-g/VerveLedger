package com.griffin.core.network.source.type

import com.griffin.core.data.model.Resource
import com.griffin.core.data.model.TypeModel

interface TypeDataSource {

    /**
     * 获取消费类型列表
     *
     * @param typeTag 类型标签（0：支出，1：收入）
     */
    suspend fun getPayTypeAndChild(typeTag: String): Resource<List<TypeModel>>

}