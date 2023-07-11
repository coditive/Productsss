package com.example.productsss.data.repository

import com.example.productsss.data.Resource
import com.example.productsss.data.local.ProductListDao
import com.example.productsss.data.local.model.Product
import com.example.productsss.data.remote.ApiService
import com.example.productsss.data.remote.model.AddProductToServerRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class DataRepository @Inject constructor(
    private val productListDao: ProductListDao,
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataRepositorySource {

    override fun fetchProductListData(): Flow<Resource<List<Product>>> {
        return flow {
            emit(apiService.getProductList())
        }.map { productListResponse ->
            Timber.d("Product list -> $productListResponse")
            if (productListResponse.isSuccessful && productListResponse.body() != null) {
                val productList = mutableListOf<Product>()
                productListResponse.body()?.forEach {
                    val product = Product(Random.nextLong(), it.image, it.price, it.productName, it.productType, it.tax)
                    productListDao.insertProduct(product)
                    productList.add(product)
                }
                Resource.Success(productList.toList())
            } else {
                Resource.DataError(404)
            }
        }.flowOn(dispatcher)
    }

    override fun addProductToProductList(addProductToServerRequest: AddProductToServerRequest): Flow<Resource<String>> {
        return flow {
            emit(apiService.addProductToServer(addProductToServerRequest))
        }.map {addProductResponse ->
            if(addProductResponse.isSuccessful) {
                Resource.Success(addProductResponse.message())
            } else {
                Resource.DataError(addProductResponse.code())
            }
        }.flowOn(dispatcher)
    }
}