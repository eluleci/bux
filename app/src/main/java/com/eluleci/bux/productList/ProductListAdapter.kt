package com.eluleci.bux.productList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eluleci.bux.R
import com.eluleci.bux.data.Product
import kotlinx.android.synthetic.main.list_item_product.view.*

class ProductListAdapter(private val weekProduct: List<Product>,
                         private val itemClick: (Product) -> Unit) :
        RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_product,
                        parent,
                        false
                ),
                itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(weekProduct[position])
    }

    override fun getItemCount() = weekProduct.size

    class ViewHolder(view: View, private val itemClick: (Product) -> Unit)
        : RecyclerView.ViewHolder(view) {

        fun bindProduct(forecast: Product) {
            with(forecast) {
                itemView.name.text = displayName
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}
