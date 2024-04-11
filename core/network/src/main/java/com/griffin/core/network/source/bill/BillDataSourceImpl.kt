package com.griffin.core.network.source.bill

import com.griffin.core.data.dto.AddBillDto
import com.griffin.core.data.dto.BillDetailDto
import com.griffin.core.data.mappers.toBillListModelDetail
import com.griffin.core.data.mappers.toBillModel
import com.griffin.core.data.model.BillListModel
import com.griffin.core.data.model.BillModel
import com.griffin.core.data.model.Resource
import com.griffin.core.data.model.toResource
import com.griffin.core.network.api.BillApi
import com.griffin.core.network.constant.toNetError
import javax.inject.Inject

class BillDataSourceImpl @Inject constructor(
    private val billApi: BillApi
) : BillDataSource {
    override suspend fun bill(
        accountType: String?,
        beginTime: String?,
        billName: String?,
        endTime: String?,
        typeTag: String?
    ): Resource<List<BillModel>> {
        return try {
            billApi.bill(accountType, beginTime, billName, endTime, typeTag).toResource {
                it.toBillModel()
            }
        } catch (e: Exception) {
            e.toNetError()
        }
    }

    override suspend fun addBill(
        accountId: Long?,
        address: String?,
        billAmount: Long,
        billId: Long?,
        billName: String,
        createTime: String,
        latitude: Double,
        longitude: Double,
        remark: String,
        typeId: Long
    ): Resource<String> {
        return try {
            billApi.addBill(
                AddBillDto(
                    accountId = accountId,
                    address = address,
                    billAmount = billAmount,
                    billId = billId,
                    billName = billName,
                    createTime = createTime,
                    latitude = latitude,
                    longitude = longitude,
                    remark = remark,
                    typeId = typeId
                )
            ).toResource()
        } catch (e: Exception) {
            e.toNetError()
        }
    }

    override suspend fun billDetail(id: Long): Resource<BillListModel> {
        return try {
            billApi.billDetail(id).toResource{
                it.toBillListModelDetail()
            }
        }catch (e: Exception){
            e.toNetError()
        }
    }

}