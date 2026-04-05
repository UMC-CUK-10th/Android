package com.example.week02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week02.databinding.FragmentPurchaseBinding
import com.google.android.material.tabs.TabLayoutMediator

class PurchaseFragment : Fragment() {

    lateinit var binding: FragmentPurchaseBinding
    private val tabTitles = arrayOf("전체", "Top & T-shirts", "Sale")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PurchaseViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position] // 각 탭에 이름표 달아주기
        }.attach()
    }
}