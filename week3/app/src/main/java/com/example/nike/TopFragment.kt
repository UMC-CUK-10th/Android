package com.example.nike

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nike.databinding.FragmentTopBinding // 레이아웃 이름에 주의!
import kotlinx.coroutines.launch

class TopFragment : Fragment(R.layout.fragment_top) { // XML 연결

    private var _binding: FragmentTopBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTopBinding.bind(view)
        preferenceManager = PreferenceManager(requireContext())

        // 기존 BuyFragment에 있던 DataStore 관찰 로직
        viewLifecycleOwner.lifecycleScope.launch {
            preferenceManager.wishListFlow.collect { savedList ->
                if (savedList.isEmpty()) {
                    setupRecyclerView(ArrayList(getDefaultList()))
                } else {
                    setupRecyclerView(ArrayList(savedList))
                }
            }
        }
    }

    private fun setupRecyclerView(list: ArrayList<Product>) {
        val adapter = ProductAdapter(list, isHome = false, isWish = false) { _ ->
            viewLifecycleOwner.lifecycleScope.launch {
                preferenceManager.saveWishList(list)
            }
        }
        binding.rvTop.layoutManager = GridLayoutManager(context, 2)
        binding.rvTop.adapter = adapter
    }

    private fun getDefaultList() = listOf(
        Product("Nike Everyday Plus Cushioned", "Training Ankle Socks (6 Pairs)", "5 Colours", "US$10", R.drawable.socks, false, false),
        Product("Nike Elite Crew", "Basketball Socks", "7 Colours", "US$16", R.drawable.socks2, false, false),
        Product("Nike Air Force", "Women's Shoes", "5 Colours", "US$115", R.drawable.airforce1, false, false),
        Product("Jordan ENike Air Force 1 '07ssentials", "Men's Shoes", "2 Colours", "US$115", R.drawable.airforce11, false, false)

    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}