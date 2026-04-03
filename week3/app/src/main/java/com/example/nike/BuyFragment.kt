package com.example.nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nike.databinding.FragmentBuyBinding

class BuyFragment : Fragment() {

    private var _binding: FragmentBuyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buyList = arrayListOf(
            Product("Nike Everyday Plus Cushioned", "Training Ankle Socks(6 Pairs)", "5 Colours", "US$10", R.drawable.socks, false),
            Product("Nike Elite Crew", "Basketball Socks", "7 Colours", "US$16", R.drawable.socks2, false),
            Product("Nike Air Force 1 '07", "Women's Shoes", "5 Colours", "US$115", R.drawable.airforce1, true),
            Product("Jordan ENike Air Force 1 '07ssentials", "Men's Shoes", "2 Colours", "US$115", R.drawable.airforce11, true)
        )

        val productAdapter = ProductAdapter(buyList, false)

        binding.rvProductBuy.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}