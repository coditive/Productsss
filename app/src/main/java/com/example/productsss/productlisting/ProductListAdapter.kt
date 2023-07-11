package com.example.productsss.productlisting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.productsss.R
import com.example.productsss.data.local.model.Product

class ProductListAdapter : ListAdapter<Product, ProductListViewHolder>(CALLBACK) {
    companion object {

        val CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.productName == newItem.productName
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_item_product_listing, parent, false)
        return ProductListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(product: Product) {

        val imageView = itemView.findViewById<ImageView>(R.id.productImage)
        Glide.with(itemView.context)
            .load(product.image)
            .apply(
                RequestOptions()
                    .placeholder(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.baseline_image_24
                        )
                    )
                    .error(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.baseline_broken_image_24
                        )
                    )
            )
            .into(imageView)

        itemView.findViewById<TextView>(R.id.productName).text = product.productName

        itemView.findViewById<TextView>(R.id.productType).text = product.productType

        itemView.findViewById<TextView>(R.id.productPrice).text = product.price.toString()

        itemView.findViewById<TextView>(R.id.productTax).text = product.tax.toString()

    }

}




