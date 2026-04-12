package com.example.nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nike.databinding.FragmentBuyBinding
import com.google.android.material.tabs.TabLayoutMediator

class BuyFragment : Fragment() {

    private var _binding: FragmentBuyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 어댑터 연결 (프래그먼트 안의 프래그먼트이므로 this 전달)
        val pagerAdapter = BuyPagerAdapter(this)
        binding.viewPagerBuy.adapter = pagerAdapter

        // 2. 탭 레이아웃과 뷰페이저2 연결
        val tabTitles = listOf("Tops", "T-Shirts", "Sale")
        TabLayoutMediator(binding.tabLayoutBuy, binding.viewPagerBuy) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}