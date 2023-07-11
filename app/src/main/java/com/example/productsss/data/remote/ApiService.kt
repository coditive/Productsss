package com.example.productsss.data.remote

import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.example.productsss.data.remote.model.ProductServerModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("api/public/get")
    suspend fun getProductList(): Response<List<ProductServerModel>>

    @Multipart
    @POST("api/public/add")
    suspend fun addProductToServerWithPhoto(
    @Part("addRequest") addRequest: RequestBody,
    @Part files: MultipartBody.Part
    ): Response<String>

    @POST("api/public/add")
    suspend fun addProductToServer(
        @Body productData: AddProductToServerRequest
    ): Response<String>




}
