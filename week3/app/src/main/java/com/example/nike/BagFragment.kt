package com.example.nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nike.databinding.FragmentBagBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BagFragment : Fragment() {

    // 바인딩 객체 선언
    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 바인딩 초기화
        _binding = FragmentBagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnOrder.setOnClickListener {
            // 장바구니에 있는 버튼을 누를 경우 구매하기로 이동
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, BuyFragment())
                .commit()

            val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
            navBar?.selectedItemId = R.id.menu_buy
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}