package com.example.productsss.productadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsss.data.Resource
import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.example.productsss.data.repository.DataRepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProductAddViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource
): ViewModel() {

    val addProductResponse = MutableStateFlow<Resource<String>>(Resource.Success(""))
    fun addProduct(addProductToServerRequest: AddProductToServerRequest) {
        addProductResponse.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("Add Product Called!!!")
            dataRepository.addProductToProductList(addProductToServerRequest).collect{
                addProductResponse.value = it
            }
        }
    }

}