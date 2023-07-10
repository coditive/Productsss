package com.example.productsss.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val productId: Long,
    val image: String,
    val price: Long,
    val productName: String,
    val productType: String,
    val tax: Long
)
