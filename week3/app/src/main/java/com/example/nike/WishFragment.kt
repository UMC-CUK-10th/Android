package com.example.nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nike.databinding.FragmentWishBinding

class WishFragment : Fragment() {
    // ViewBinding 설정
    private var _binding: FragmentWishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 딱 2개의 위시리스트 상품 데이터 준비 (데이터 7개 확인!)
        // 순서: brand, name, category, price, imageRes, isBest, isFavorite
        val wishList = arrayListOf(
            Product("Air Jordan 1 Mid", "category", "colours", "US$125", R.drawable.socks, true, true),
            Product("Nike Everyday Plus Cushioned", "Training Ankle socks (6 Pairs)", "5 Colours", "US$10", R.drawable.jordan1, false, true)
        )

        // 2. 어댑터 연결 (isHome = false -> 그리드용 4줄 레이아웃 사용)
        val wishAdapter = ProductAdapter(wishList, false)
        binding.rvWish.adapter = wishAdapter

        // 3. ⭐ 가로 2칸 격자(Grid)로 배치하기
        binding.rvWish.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}