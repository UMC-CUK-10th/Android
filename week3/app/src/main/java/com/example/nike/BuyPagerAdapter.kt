package com.example.nike

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BuyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // 탭의 개수 (Tops, T-shirts, Sale)
    override fun getItemCount(): Int = 3

    // 위치에 따라 보여줄 프래그먼트 연결
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TopFragment()
            1 -> TshirtFragment() // ⭐ 'TshirtFragment'인지 'TshirtsFragment'인지 확인!
            else -> SaleFragment()
        }
    }
}