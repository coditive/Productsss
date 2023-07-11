package com.example.productsss.productlisting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsss.data.Resource
import com.example.productsss.data.local.model.Product
import com.example.productsss.data.repository.DataRepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource
): ViewModel() {

    val productListData = MutableStateFlow<Resource<List<Product>>>(Resource.Success(listOf()))
    fun getProductList() {
        viewModelScope.launch {
            productListData.value = Resource.Loading()
            dataRepository.fetchProductListData().collect {
                productListData.value = it
            }
        }
    }

}