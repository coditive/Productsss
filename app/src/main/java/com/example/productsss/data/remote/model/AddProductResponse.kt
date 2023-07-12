package com.example.productsss.data.remote.model

import com.squareup.moshi.Json

data class AddProductResponse (
    val  message: String,
    @Json(name = "product_details") val productDetails: ProductServerModel? = null,
    @Json(name = "product_id") val productId: Long? = null,
    val success: Boolean
    )