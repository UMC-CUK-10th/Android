package com.example.week02

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PurchaseViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // 탭이 총 몇 개인지? (3개)
    override fun getItemCount(): Int = 3

    // 각 위치(포지션)에 어떤 프래그먼트를 띄워줄지?
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PurchaseAllFragment()   // 첫 번째 탭: 원래 있던 리사이클러뷰 화면
            1 -> PurchaseTopFragment()  // 두 번째 탭: 빈 화면
            2 -> PurchaseSaleFragment()  // 세 번째 탭: 빈 화면
            else -> PurchaseAllFragment()
        }
    }
}