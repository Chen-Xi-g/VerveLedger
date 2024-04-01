package com.griffin.core.network.source.type

import com.griffin.core.data.mappers.toTypeModel
import com.griffin.core.data.model.Resource
import com.griffin.core.data.model.TypeModel
import com.griffin.core.data.model.toResource
import com.griffin.core.network.api.TypeApi
import com.griffin.core.network.constant.toNetError
import javax.inject.Inject

class TypeDataSourceImpl @Inject constructor(
    private val typeApi: TypeApi
) : TypeDataSource {
    override suspend fun getPayTypeAndChild(typeTag: String): Resource<List<TypeModel>> {
        return try {
            typeApi.getPayTypeAndChild(typeTag).toResource{
                it.toTypeModel()
            }
        } catch (e: Exception) {
            e.toNetError()
        }
    }
}