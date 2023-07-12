package com.example.productsss.data.remote.model

import com.squareup.moshi.Json

data class AddProductToServerRequest (
    @Json(name = "product_name") val productName: String,
    @Json(name = "product_type") val productType: String,
    @Json(name = "price")val price: String,
    @Json(name =  "tax") val tax: String,
    @Json(name = "files[]")val files: String? = ""
    )

fun AddProductToServerRequest.toMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    map["product_name"] = productName
    map["product_type"] = productType
    map["price"] = price
    map["tax"] = tax
    map["files[]"] = files ?: ""
    return map
}
