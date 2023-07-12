package com.example.productsss.data.repository

import com.example.productsss.data.Resource
import com.example.productsss.data.local.model.Product
import com.example.productsss.data.remote.model.AddProductToServerRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DataRepositorySource {

    fun fetchProductListData(): Flow<Resource<List<Product>>>

    fun addProductToProductList(addProductToServerRequest: AddProductToServerRequest, file: File?): Flow<Resource<String>>

    fun performSearchInDB(query: String): Flow<List<Product>>

}