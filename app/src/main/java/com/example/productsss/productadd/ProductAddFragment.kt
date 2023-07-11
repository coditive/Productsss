package com.example.productsss.productadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.productsss.R
import com.example.productsss.data.Resource
import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProductAddFragment: Fragment() {

    private val viewModel by viewModels<ProductAddViewModel>()
    private lateinit var productName: EditText
    private lateinit var productType: EditText
    private lateinit var productPrice: EditText
    private lateinit var productTax: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productName = view.findViewById(R.id.productNameEdt)
        productPrice = view.findViewById(R.id.productPriceEdt)
        productType = view.findViewById(R.id.productTypeEdt)
        productTax = view.findViewById(R.id.productTaxEdt)

        view.findViewById<Button>(R.id.addProductButton).setOnClickListener {
            view.hideKeyboard()
            val product = AddProductToServerRequest(
                productName.text.toString(),
                productType.text.toString(),
                productPrice.text.toString(),
                productTax.text.toString(),
            )
            viewModel.addProduct(product)
        }


        viewModel.addProductResponse.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.DataError -> {
                    view.showSnackbar("Something Wrong happened!!", Snackbar.LENGTH_SHORT)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (it.data?.isNotEmpty() == true) {
                        Timber.d("Request made successfully -> ${it.data}")
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }
}