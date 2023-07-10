package com.example.productsss.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productsss.data.local.model.Product


@Database(entities = [Product::class], version = 1)
abstract class ProductDB: RoomDatabase() {
    abstract fun productListDao(): ProductListDao
}