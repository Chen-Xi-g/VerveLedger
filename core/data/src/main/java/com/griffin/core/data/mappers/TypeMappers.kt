package com.griffin.core.data.mappers

import com.griffin.core.data.dto.UserPayTypeDto
import com.griffin.core.data.model.TypeChildModel
import com.griffin.core.data.model.TypeModel

/**
 * 类型数据传输对象转换为类型模型
 */
fun List<UserPayTypeDto>?.toTypeModel(): List<TypeModel>{
    return if (isNullOrEmpty()){
        return emptyList()
    }else{
        map { map ->
            TypeModel(
                parentId = map.parentId,
                typeId = map.typeId,
                typeName = map.typeName,
                typeTag = map.typeTag,
                itemChildList = map.child.toTypeChildModel()
            )
        }
    }
}

/**
 * 类型数据传输对象转换为类型子模型
 */
fun List<UserPayTypeDto>?.toTypeChildModel(): List<TypeChildModel>{
    return if (isNullOrEmpty()) {
        return emptyList()
    }else{
        map { map ->
            TypeChildModel(
                parentId = map.parentId,
                typeId = map.typeId,
                typeName = map.typeName,
                typeTag = map.typeTag
            )
        }
    }
}