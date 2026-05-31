package com.example.nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.nike.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())

        // ⭐ 3. wishListFlow 대신 homeListFlow를 관찰합니다!
        viewLifecycleOwner.lifecycleScope.launch {
            preferenceManager.homeListFlow.collect { savedList ->
                if (savedList.isEmpty()) {
                    setupRecyclerView(ArrayList(getDefaultList()))
                } else {
                    setupRecyclerView(ArrayList(savedList))
                }
            }
        }
    }

    private fun setupRecyclerView(list: ArrayList<Product>) {
        val productAdapter = ProductAdapter(list, isHome = true, isWish = false) { clickedProduct ->
            // ⭐ 하트 클릭 시 saveWishList 대신 saveHomeList를 호출합니다!
            viewLifecycleOwner.lifecycleScope.launch {
                preferenceManager.saveHomeList(list)
            }
        }

        binding.product.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setPadding(60, 0, 60, 0)
            clipToPadding = false

            if (onFlingListener == null) {
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(this)
            }
        }
    }

    private fun getDefaultList() = listOf(
        Product("Air Jordan XXXVI", "Basketball Shoes", "1 Colour", "US$185", R.drawable.af1, false, false),
        Product("Nike Air Force 1 '07", "Men's Shoes", "2 Colours", "US$115", R.drawable.afw1, false, false)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}