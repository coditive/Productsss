package com.example.productsss.productadd

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsss.data.Resource
import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.example.productsss.data.repository.DataRepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ProductAddViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource
): ViewModel() {

    private val _addProductDataFlow = MutableStateFlow(AddProductData.getInitDataModel())
    val addProductDataFlow: StateFlow<AddProductData> = _addProductDataFlow

    val addProductResponse = MutableStateFlow<Resource<String>>(Resource.Success(""))
    fun addProduct(addProductToServerRequest: AddProductToServerRequest, file: File?) {
        addProductResponse.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("Add Product Called!!!")
            dataRepository.addProductToProductList(addProductToServerRequest, file).collect{
                addProductResponse.value = it
            }
        }
    }

   fun setProductName(productName: String) {
       if(productName.isNotEmpty()) {
           _addProductDataFlow.value = _addProductDataFlow.value.copy(
               productName = productName, errorField = ErrorFieldEnum.RE_PRODUCT_NAME, errorMessage = ""
           )
       } else {
           _addProductDataFlow.value = _addProductDataFlow.value.copy(
               productName = productName, errorField = ErrorFieldEnum.PRODUCT_NAME, errorMessage = "Product Name is Empty!"
           )
       }
   }

    fun setProductType(productType: String) {
        if(productType.isNotEmpty()) {
            _addProductDataFlow.value = _addProductDataFlow.value.copy(
                productType = productType, errorField = ErrorFieldEnum.RE_PRODUCT_TYPE, errorMessage = ""
            )
        } else {
            _addProductDataFlow.value = _addProductDataFlow.value.copy(
                productType = productType, errorField = ErrorFieldEnum.PRODUCT_TYPE, errorMessage = "Product Type is Empty!"
            )
        }
    }

    fun setProductPrice(price: String) {
      if(price.isEmpty()) {
            _addProductDataFlow.value = _addProductDataFlow.value.copy(
                price = price, errorField = ErrorFieldEnum.PRODUCT_PRICE, errorMessage = "Price is Empty"
            )
        } else if(price.isDigitsOnly()) {
          _addProductDataFlow.value = _addProductDataFlow.value.copy(
              price = price, errorField = ErrorFieldEnum.RE_PRODUCT_PRICE, errorMessage = ""
          )
      }  else {
            _addProductDataFlow.value = _addProductDataFlow.value.copy(
                price = price, errorField = ErrorFieldEnum.PRODUCT_PRICE, errorMessage = "Price should not contain letters!"
            )
        }
    }

    fun setProductTax(productTax: String) {
        if(productTax.isEmpty()) {
            _addProductDataFlow.value = _addProductDataFlow.value.copy(
                tax = productTax, errorField = ErrorFieldEnum.PRODUCT_TAX, errorMessage = "Tax is Empty"
            )
        } else if(productTax.isDigitsOnly()) {
            _addProductDataFlow.value = _addProductDataFlow.value.copy(
                tax = productTax, errorField = ErrorFieldEnum.RE_PRODUCT_TAX, errorMessage = ""
            )
        }  else {
            _addProductDataFlow.value = _addProductDataFlow.value.copy(
                tax = productTax, errorField = ErrorFieldEnum.PRODUCT_TAX, errorMessage = "Tax should not contain letters!"
            )
        }
    }
}


data class AddProductData(
    val productName: String,
    val productType: String,
    val price: String,
    val tax: String,
    val errorMessage: String,
    val errorField: ErrorFieldEnum
) {
    companion object {
        fun getInitDataModel(): AddProductData {
            return AddProductData("", "", "", "",
                "", ErrorFieldEnum.NONE)
        }
    }
}

enum class ErrorFieldEnum {
    NONE, PRODUCT_NAME, PRODUCT_TYPE, PRODUCT_PRICE, PRODUCT_TAX,
    RE_PRODUCT_NAME, RE_PRODUCT_TYPE, RE_PRODUCT_PRICE, RE_PRODUCT_TAX
}
