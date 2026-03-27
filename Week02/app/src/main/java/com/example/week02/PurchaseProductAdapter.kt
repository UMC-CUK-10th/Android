package com.example.week02

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week02.databinding.ItemPurchaseBinding

class PurchaseProductAdapter(
    private val productList: MutableList<PurchaseProductData>,
    private val onItemClicked: (PurchaseProductData)-> Unit
): RecyclerView.Adapter<PurchaseProductViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseProductViewHolder {
        val binding = ItemPurchaseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return PurchaseProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PurchaseProductViewHolder,
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

class PurchaseProductViewHolder(val binding: ItemPurchaseBinding):
        RecyclerView.ViewHolder(binding.root){
    fun bind(product: PurchaseProductData){
        binding.purchaseImage.setImageResource(product.image)
        binding.purchaseName.text = product.name
        binding.purchaseSubtitle.text = product.subtitle
        binding.purchaseColors.text = product.colors
        binding.purchasePrice.text = product.price

        if(product.isBestSeller){
            binding.purchaseBestseller.visibility = View.VISIBLE
        }
        else{
            binding.purchaseBestseller.visibility = View.GONE
        }

        if(product.isWished){
            binding.purchaseHeartButton.setImageResource(R.drawable.heart)
        }
        else{
            binding.purchaseHeartButton.setImageResource(R.drawable.blankheart)
        }
        binding.purchaseHeartButton.setOnClickListener {
            product.isWished = !product.isWished

            if (product.isWished) {
                binding.purchaseHeartButton.setImageResource(R.drawable.heart)
            } else {
                binding.purchaseHeartButton.setImageResource(R.drawable.blankheart)
            }
        }
    }
}