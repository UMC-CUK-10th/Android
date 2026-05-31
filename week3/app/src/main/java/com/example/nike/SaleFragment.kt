package com.example.nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SaleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // 우선은 텍스트뷰 하나만 있는 간단한 레이아웃을 연결하거나,
        // 전용 XML(fragment_top.xml)을 만들어서 연결해 주세요.
        return inflater.inflate(R.layout.fragment_sale, container, false)
    }
}