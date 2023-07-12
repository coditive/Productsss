package com.example.productsss.productlisting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsss.data.Resource
import com.example.productsss.data.local.model.Product
import com.example.productsss.data.repository.DataRepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource
) : ViewModel() {

    private val productListData =
        MutableStateFlow<Resource<List<Product>>>(Resource.Success(listOf()))
    private val queryData = MutableStateFlow("")

    val productList: StateFlow<Resource<List<Product>>> =
        combine(productListData, queryData) { productList, query ->
            Timber.tag("ProductData").d("Product List -> ${productList.data?.size} && query -> $query")
            return@combine if(query.isEmpty()) {
                Timber.tag("ProductData").d("Product List -> ${productList.data?.size} && query empty -> $query")
                productList
            } else {
                Timber.tag("ProductData").d("Product List -> ${productList.data?.size} && query full -> $query")
                Resource.Success(dataRepository.performSearchInDB(query).first())
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, Resource.Loading())

    fun getProductList() {
        viewModelScope.launch {
            productListData.value = Resource.Loading()
            dataRepository.fetchProductListData().collect {
                Timber.d("DataRepository is called!!! , data -> $it")
                productListData.value = it
            }
        }
    }

    fun performSearch(query: String?) {
        queryData.value = query ?: ""
    }

}