package com.example.productsss.data.remote

import com.example.productsss.data.local.model.Product
import retrofit2.http.GET


interface ApiService {

    @GET("api/public/get")
    suspend fun getProductList(): Response<List<Product>>
}