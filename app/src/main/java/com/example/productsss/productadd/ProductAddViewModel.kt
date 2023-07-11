package com.example.productsss.productadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.example.productsss.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductAddViewModel @Inject constructor(
    private val dataRepository: DataRepository
): ViewModel() {

    fun addProduct(addProductToServerRequest: AddProductToServerRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.addProductToProductList(addProductToServerRequest)
        }
    }

}