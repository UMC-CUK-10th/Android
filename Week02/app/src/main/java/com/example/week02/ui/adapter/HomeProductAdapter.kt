package com.example.week02.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week02.model.HomeProductData
import com.example.week02.databinding.ItemProductBinding

class HomeProductAdapter(
    private val productList: List<HomeProductData>,
    private val onItemClicked:(HomeProductData)-> Unit
): RecyclerView.Adapter<NewProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return NewProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
        holder.binding.root.setOnClickListener {
            onItemClicked(product)
        }
    }

    override fun getItemCount(): Int = productList.size
    }


class NewProductViewHolder(val binding: ItemProductBinding):
    RecyclerView.ViewHolder(binding.root){
    fun bind(product: HomeProductData){
        binding.productImageShoes1.setImageResource(product.image)
        binding.productNameShoes1.text = product.name
        binding.productPriceShoes1.text = product.price
    }
}