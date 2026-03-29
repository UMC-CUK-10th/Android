package com.example.week02

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week02.databinding.ItemWishlistBinding

class WishlistProductAdapter (
    private val productList: MutableList<WishlistProductData>,
    private val onItemClicked: (WishlistProductData)-> Unit
): RecyclerView.Adapter<WishlistProductViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistProductViewHolder {
        val binding = ItemWishlistBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return WishlistProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WishlistProductViewHolder,
        position: Int,
    ) {
        val product = productList[position]
        holder.bind(product)
        holder.binding.root.setOnClickListener{
            onItemClicked(product)
        }
    }

    override fun getItemCount(): Int = productList.size
}

class WishlistProductViewHolder(val binding: ItemWishlistBinding):
        RecyclerView.ViewHolder(binding.root){

    fun bind(product: WishlistProductData){
        binding.wishlistImage.setImageResource((product.image))
        binding.wishlistName.text = product.name
        binding.wishlistSubtitle.text = product.subtitle
        binding.wishlistColors.text = product.colors
        binding.wishlistPrice.text = product.price
    }
}