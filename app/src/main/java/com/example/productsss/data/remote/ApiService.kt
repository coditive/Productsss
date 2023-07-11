package com.example.productsss.data.remote

import com.example.productsss.data.remote.model.ProductServerModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/public/get")
    suspend fun getProductList(): Response<List<ProductServerModel>>
}