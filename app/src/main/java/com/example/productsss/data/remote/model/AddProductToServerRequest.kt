package com.example.productsss.data.remote.model

import com.squareup.moshi.Json
import java.io.File

data class AddProductToServerRequest (
    @Json(name = "product_name") val productName: String,
    @Json(name = "product_type") val productType: String,
    @Json(name = "price")val price: String,
    @Json(name =  "tax") val tax: String,
    @Json(name = "files[]") val files: List<File>?
    )