package com.example.productsss.productadd

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.productsss.R
import com.example.productsss.data.Resource
import com.example.productsss.data.remote.model.AddProductToServerRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@AndroidEntryPoint
class ProductAddFragment : Fragment() {

    private val viewModel by viewModels<ProductAddViewModel>()

    private val REQUEST_CODE_PICK_IMAGE = 100
    private var file: File? = null
    private lateinit var productName: EditText
    private lateinit var productType: EditText
    private lateinit var productPrice: EditText
    private lateinit var productTax: EditText
    private lateinit var openGalleryImageView: ImageView
    private lateinit var selectedPhotoImageView: ImageView
    private lateinit var addProductButton: Button

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
        openGalleryImageView = view.findViewById(R.id.openGalleryButton)
        selectedPhotoImageView = view.findViewById(R.id.productImage)
        addProductButton = view.findViewById(R.id.addProductButton)

        addObserversOnEditTextFields()

        addListeners()

    }

    private fun addListeners() {
        addProductButton.setOnClickListener {
            view?.hideKeyboard()
            val product = AddProductToServerRequest(
                productName.text.toString(),
                productType.text.toString(),
                productPrice.text.toString(),
                productTax.text.toString()
            )
            viewModel.addProduct(product, file)
        }

        openGalleryImageView.setOnClickListener {
            openGallery()
        }

        viewModel.addProductResponse.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.DataError -> {
                    view?.showSnackbar("Some Thing Went Wrong !", Snackbar.LENGTH_SHORT)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (it.data?.isNotEmpty() == true) {
                        Timber.d("Request made successfully -> ${it.data}")
                        view?.showSnackbar("Product Added SuccessFully !!", Snackbar.LENGTH_SHORT)
                        findNavController().navigateUp()
                    }
                }
            }
        }

        viewModel.addProductDataFlow.asLiveData().observe(viewLifecycleOwner) {
            when (it.errorField) {
                ErrorFieldEnum.NONE -> {}
                ErrorFieldEnum.PRODUCT_NAME -> {
                    productName.error = it.errorMessage
                }
                ErrorFieldEnum.PRODUCT_TYPE -> {
                    productType.error = it.errorMessage
                }
                ErrorFieldEnum.PRODUCT_PRICE -> {
                    productPrice.error = it.errorMessage
                }
                ErrorFieldEnum.PRODUCT_TAX -> {
                    productTax.error = it.errorMessage
                }
                ErrorFieldEnum.RE_PRODUCT_NAME -> {
                    productName.error = null
                }
                ErrorFieldEnum.RE_PRODUCT_TYPE -> {
                    productType.error = null
                }
                ErrorFieldEnum.RE_PRODUCT_PRICE -> {
                    productPrice.error = null
                }
                ErrorFieldEnum.RE_PRODUCT_TAX -> {
                    productTax.error = null
                }
            }
        }
    }

    private fun areFieldsWithErrors(): Boolean =
        TextUtils.isEmpty(productName.text.toString().trim { it <= ' ' }) || TextUtils.isEmpty(
            productType.text.toString().trim { it <= ' ' }) ||
                TextUtils.isEmpty(
                    productPrice.text.toString().trim { it <= ' ' }) || TextUtils.isEmpty(
            productTax.text.toString().trim { it <= ' ' })


    private fun addObserversOnEditTextFields() {
        productName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setProductName(s.toString())
                addProductButton.isEnabled = !areFieldsWithErrors()
            }

        })

        productPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setProductPrice(s.toString())
                addProductButton.isEnabled = !areFieldsWithErrors()
            }

        })

        productType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setProductType(s.toString())
                addProductButton.isEnabled = !areFieldsWithErrors()
            }

        })

        productTax.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setProductTax(s.toString())
                addProductButton.isEnabled = !areFieldsWithErrors()
            }

        })


    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!
            val fileName: String = getFilePathFromUri(requireContext(), selectedImageUri)!!
            Timber.d("FileName = $fileName")
            file = File(fileName)
            Glide.with(requireContext()).load(file).into(selectedPhotoImageView)
        }
    }

    // Function to get the file path from a content URI
    private fun getFilePathFromUri(context: Context, uri: Uri): String? {
        if (uri == null) return null

        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            val outputFile = createTempFile(context, "temp_image", ".JPEG")
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // Adjust buffer size as needed
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                return outputFile.absolutePath
            }
        }
        return null
    }

    // Function to create a temporary file
    private fun createTempFile(context: Context, prefix: String, extension: String): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(prefix, extension, dir)
    }

}