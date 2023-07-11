package com.example.productsss.productlisting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productsss.R
import com.example.productsss.data.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListingFragment : Fragment() {

    private lateinit var productListRv: RecyclerView
    private lateinit var productAdapter: ProductListAdapter
    private val viewModel by viewModels<ProductListViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productListRv = view.findViewById(R.id.productListRv)
        productAdapter = ProductListAdapter()
        productListRv.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        viewModel.getProductList()

        view.findViewById<FloatingActionButton>(R.id.addProductFab).setOnClickListener {
            findNavController().navigate(R.id.action_productListingFragment_to_productAddFragment)
        }

        viewModel.productListData.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.DataError -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (it.data != null)
                        productAdapter.submitList(it.data)
                }
            }
        }

    }
}