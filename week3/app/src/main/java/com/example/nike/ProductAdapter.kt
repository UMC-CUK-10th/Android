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
    private val isHome: Boolean
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
            // --- 홈 화면 로직 ---
            binding.tvName.text = product.name
            binding.tvPrice.text = product.price
            binding.ivProduct.setImageResource(product.imageRes)

        } else if (!isHome && binding is ItemProductGridBinding) {
            // --- 구매 탭 & 위시리스트 로직 (Grid Layout) ---

            // 1. 공통 설정: 위시리스트라면 하트 숨기기 (구매 탭에서는 보임)
            // WishFragment에서 호출할 때 리스트 크기가 2개뿐이라면 위시리스트로 간주하고 하트를 숨깁니다.
            // 만약 구매 탭에서도 하트를 숨기고 싶은 게 아니라면 아래 조건을 확인해 보세요.
            if (productList.size == 2) {
                binding.ivHeart.visibility = View.GONE
                binding.tvBest.visibility = View.GONE // 위시리스트에선 베스트셀러도 숨김
            } else {
                binding.ivHeart.visibility = View.VISIBLE
            }

            // 2. 위치(position)에 따른 분기 처리
            if (productList.size == 2 && position == 0) {
                // 첫 번째 제품: 이름과 가격만 표시
                binding.tvName.text = product.name
                binding.tvPrice.text = product.price
                binding.ivProduct.setImageResource(product.imageRes)

                // 카테고리와 컬러 숨기기
                binding.tvCategory.visibility = View.GONE
                binding.tvColors.visibility = View.GONE
            } else {
                // 두 번째 제품 (또는 일반 구매 탭 상품): 전체 표시
                binding.tvName.text = product.name
                binding.tvCategory.text = product.category
                binding.tvColors.text = product.colors
                binding.tvPrice.text = product.price
                binding.ivProduct.setImageResource(product.imageRes)

                // 숨겨졌을 수도 있으니 다시 보이게 설정
                binding.tvCategory.visibility = View.VISIBLE
                binding.tvColors.visibility = View.VISIBLE

                // 베스트셀러 표시 (위시리스트가 아닐 때만)
                if (productList.size != 2) {
                    binding.tvBest.visibility = if (product.isBest) View.VISIBLE else View.GONE
                }
            }

            // 3. 하트 아이콘 상태 및 클릭 리스너 (구매 탭 전용)
            if (binding.ivHeart.visibility == View.VISIBLE) {
                if (product.isFavorite) {
                    binding.ivHeart.setImageResource(R.drawable.filledheartlogo)
                } else {
                    binding.ivHeart.setImageResource(R.drawable.heartlogo)
                }

                binding.ivHeart.setOnClickListener {
                    product.isFavorite = !product.isFavorite
                    if (product.isFavorite) {
                        binding.ivHeart.setImageResource(R.drawable.filledheartlogo)
                    } else {
                        binding.ivHeart.setImageResource(R.drawable.heartlogo)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}