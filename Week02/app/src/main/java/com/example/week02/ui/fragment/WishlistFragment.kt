package com.example.week02.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.week02.databinding.FragmentWishlistBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week02.repository.ProductDataManager
import com.example.week02.ui.adapter.PurchaseProductAdapter
import com.example.week02.model.PurchaseProductData
import kotlinx.coroutines.launch

class WishlistFragment : Fragment() {

    lateinit var binding: FragmentWishlistBinding
    private lateinit var dataManager: ProductDataManager
    private var allProductList : MutableList<PurchaseProductData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataManager = ProductDataManager(requireContext())

        lifecycleScope.launch {
            dataManager.getProducts().collect { savedList ->
                allProductList = savedList.toMutableList()
                val wishlistedItems = allProductList.filter { it.isWished }

                val adapter = PurchaseProductAdapter(
                    productList = wishlistedItems,
                    isWishlistMode = true,
                    onItemClicked = { product -> },
                    onHeartClicked = { product -> }
                )
                binding.wishlistRcv.adapter = adapter
                binding.wishlistRcv.layoutManager = GridLayoutManager(requireContext(),2)
            }
        }


    }

}