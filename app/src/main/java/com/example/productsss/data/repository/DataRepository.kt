package com.example.productsss.data.repository

import com.example.productsss.data.Resource
import com.example.productsss.data.local.ProductListDao
import com.example.productsss.data.local.model.Product
import com.example.productsss.data.remote.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val productListDao: ProductListDao,
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
) : DataRepositorySource {


    override suspend fun fetchProductListData(): Flow<Resource<List<Product>>> {
     return apiService.getProductList().map { productListResponse ->
            if (productListResponse.isSuccess && productListResponse.data != null) {
                productListResponse.data?.forEach {
                    productListDao.insertProduct(it)
                }
                Resource.Success(productListResponse.data!!)
            } else {
                Resource.DataError(productListResponse.code)
            }
        }.flowOn(dispatcher)
    }
}