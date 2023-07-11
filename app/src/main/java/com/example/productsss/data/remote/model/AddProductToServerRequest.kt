package com.example.productsss.data.remote.model

import com.squareup.moshi.Json

data class AddProductToServerRequest (
    @Json(name = "product_name") val productName: String,
    @Json(name = "product_type") val productType: String,
    @Json(name = "price")val price: String,
    @Json(name =  "tax") val tax: String,
    @Json(name = "files[]")val files: String? = ""
    )