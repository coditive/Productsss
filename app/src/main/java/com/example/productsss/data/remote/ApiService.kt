package com.example.productsss.data.remote

import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.example.productsss.data.remote.model.ProductServerModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("api/public/get")
    suspend fun getProductList(): Response<List<ProductServerModel>>

    @FormUrlEncoded
    @POST("api/public/add")
    suspend fun addProductToServer(
    @Body addRequest: AddProductToServerRequest
    ): Response<Nothing>

}