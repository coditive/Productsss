package com.example.productsss.data.remote

import com.example.productsss.data.remote.model.AddProductResponse
import com.example.productsss.data.remote.model.ProductServerModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("api/public/get")
    suspend fun getProductList(): Response<List<ProductServerModel>>

    @Multipart
    @POST("api/public/add")
    suspend fun addProductToServerWithPhoto(
    @Part("product_name") productName: String,
    @Part("product_type") productType: String,
    @Part("price") price: String,
    @Part("tax") tax: String,
    @Part files: MultipartBody.Part
    ): Response<AddProductResponse>

    @FormUrlEncoded
    @POST("api/public/add")
    suspend fun addProductToServer(
       @FieldMap productData: Map<String, String>
    ): Response<AddProductResponse>

}
