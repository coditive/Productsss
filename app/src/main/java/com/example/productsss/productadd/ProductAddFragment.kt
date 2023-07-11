package com.example.productsss.productadd

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
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
import java.io.File


@AndroidEntryPoint
class ProductAddFragment: Fragment() {

    private val viewModel by viewModels<ProductAddViewModel>()

    private val REQUEST_CODE_PICK_IMAGE = 100
    private var file: File? = null
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
                productTax.text.toString()
            )
            viewModel.addProduct(product, file)
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!
            val fileName: String = getFileNameFromUri(selectedImageUri)
            file = File(fileName)
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var fileName: String? = null
        if (uri.scheme == "content") {
            requireActivity().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex)
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.lastPathSegment
        }
        return fileName.toString()
    }

}