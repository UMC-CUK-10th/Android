package com.example.nike

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.nike.databinding.ItemProductGridBinding
import com.example.nike.databinding.ItemProductBinding

class ProductAdapter(
    private val productList: ArrayList<Product>,
    private val isHome: Boolean,
    private val isWish: Boolean,
    // 하트 클릭 시 실행할 함수를 인자로 받습니다.
    private val onHeartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = if (isHome) {
            ItemProductBinding.inflate(inflater, parent, false)
        } else {
            ItemProductGridBinding.inflate(inflater, parent, false)
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        val binding = holder.binding

        if (isHome && binding is ItemProductBinding) {
            bindHomeItem(binding, product)
        } else if (!isHome && binding is ItemProductGridBinding) {
            // position 조건 없이 일관되게 바인딩하기 위해 파라미터 수정
            bindGridItem(binding, product)
        }
    }

    private fun bindHomeItem(binding: ItemProductBinding, product: Product) {
        binding.tvName.text = product.name
        binding.tvPrice.text = product.price
        binding.ivProduct.setImageResource(product.imageRes)
    }

    private fun bindGridItem(binding: ItemProductGridBinding, product: Product) {
        // 1. 가시성 설정 (위시리스트에서는 하트와 BEST 태그를 숨김)
        setupGridVisibility(binding, product)

        // 2. 정보 바인딩 (위시리스트 여부와 상관없이 상세 정보를 모두 표시)
        bindFullInfo(binding, product)

        // 3. 하트 클릭 리스너 설정
        setupHeartListener(binding, product)
    }

    private fun setupGridVisibility(binding: ItemProductGridBinding, product: Product) {
        if (isWish) {
            // 위시리스트 탭에서는 하트와 베스트 태그를 GONE 처리하여 숨깁니다.
            binding.ivHeart.visibility = View.GONE
            binding.tvBest.visibility = View.GONE
        } else {
            // 일반 탭에서는 하트를 보여주고, 상품 상태에 따라 BEST 태그를 표시합니다.
            binding.ivHeart.visibility = View.VISIBLE
            binding.tvBest.visibility = if (product.isBest) View.VISIBLE else View.GONE
        }
    }

    // 상세 정보를 바인딩하는 함수 (카테고리, 색상 포함)
    private fun bindFullInfo(binding: ItemProductGridBinding, product: Product) {
        binding.tvName.text = product.name
        binding.tvCategory.text = product.category
        binding.tvColors.text = product.colors
        binding.tvPrice.text = product.price
        binding.ivProduct.setImageResource(product.imageRes)

        // 정보가 누락되지 않도록 모두 VISIBLE로 설정합니다.
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvColors.visibility = View.VISIBLE
    }

    // 하트 클릭 시 콜백 실행 로직
    private fun setupHeartListener(binding: ItemProductGridBinding, product: Product) {
        if (binding.ivHeart.visibility == View.VISIBLE) {
            updateHeartImage(binding, product.isFavorite)

            binding.ivHeart.setOnClickListener {
                product.isFavorite = !product.isFavorite
                updateHeartImage(binding, product.isFavorite)

                // 클릭된 제품 정보를 프래그먼트로 전달하여 DataStore에 저장하게 함
                onHeartClick(product)
            }
        }
    }

    private fun updateHeartImage(binding: ItemProductGridBinding, isFavorite: Boolean) {
        // 리소스 이름이 heartlogo_filled 인지 filledheartlogo 인지 확인 후 맞춰주세요!
        val resId = if (isFavorite) R.drawable.filledheartlogo else R.drawable.heartlogo
        binding.ivHeart.setImageResource(resId)
    }

    override fun getItemCount(): Int = productList.size
}