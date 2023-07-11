package com.example.productsss.data.repository

import com.example.productsss.data.Resource
import com.example.productsss.data.local.ProductListDao
import com.example.productsss.data.local.model.Product
import com.example.productsss.data.remote.ApiService
import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
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
                    val product = Product(
                        Random.nextLong(),
                        it.image,
                        it.price,
                        it.productName,
                        it.productType,
                        it.tax
                    )
                    productListDao.insertProduct(product)
                    productList.add(product)
                }
                Resource.Success(productList.toList())
            } else {
                Resource.DataError(404)
            }
        }.flowOn(dispatcher)
    }

    override fun addProductToProductList(
        addProductToServerRequest: AddProductToServerRequest,
        file: File?
    ): Flow<Resource<String>> {
        return flow {
            if (file != null) {
                val json = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    .adapter(AddProductToServerRequest::class.java)
                    .toJson(addProductToServerRequest)
                val productDataRequestBody =
                    RequestBody.create("application/json".toMediaTypeOrNull(), json)

                val imageRequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                val imagePart =
                    MultipartBody.Part.createFormData("image", file.name, imageRequestBody)
                emit(apiService.addProductToServerWithPhoto(productDataRequestBody, imagePart))
            } else {
                emit(apiService.addProductToServer(addProductToServerRequest))
            }
        }.map { addProductResponse ->
            if (addProductResponse.isSuccessful) {
                Resource.Success(addProductResponse.message())
            } else {
                Resource.DataError(addProductResponse.code())
            }
        }.flowOn(dispatcher)
    }
}