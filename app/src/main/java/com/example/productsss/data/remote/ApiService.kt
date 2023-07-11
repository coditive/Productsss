package com.example.productsss.data.remote

import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.example.productsss.data.remote.model.ProductServerModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @GET("api/public/get")
    suspend fun getProductList(): Response<List<ProductServerModel>>

    @POST("api/public/add")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun addProductToServer(
    @Body addRequest: AddProductToServerRequest
    ): Response<String>

}