package com.example.productsss.data.repository

import com.example.productsss.data.Resource
import com.example.productsss.data.local.model.Product
import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {

    fun fetchProductListData(): Flow<Resource<List<Product>>>

}