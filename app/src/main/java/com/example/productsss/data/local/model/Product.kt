package com.example.productsss.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Product(
    @PrimaryKey val productId: Long,
    val image: String,
    val price: Long,
   @Json(name = "product_name") val productName: String,
   @Json(name = "product_type") val productType: String,
    val tax: Long
)
