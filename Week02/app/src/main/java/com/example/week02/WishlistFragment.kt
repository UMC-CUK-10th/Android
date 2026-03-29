package com.example.week02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week02.databinding.FragmentWishlistBinding
import androidx.recyclerview.widget.GridLayoutManager

class WishlistFragment : Fragment() {

    lateinit var binding: FragmentWishlistBinding

    /*private val wishlistProductList = List(30){index ->
        WishlistProductData(
            image = if (index%2==0) R.drawable.shoes3 else R.drawable.socks1,
            name = "Nike test product ${index + 1}",
            subtitle = "test subtitle",
            colors = "${(index % 5) + 1} Colors",
            price = "US$${100 + index}",
        )
    }*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wishlistProductList = mutableListOf(
            WishlistProductData(R.drawable.socks1,
                "Nike Everyday Plus Cushioned",
                "Training Ankle Socks(6 Pairs)",
                "5 Colors", "US$10"),
            WishlistProductData(R.drawable.shoes5,
                "Air Jordan 1 Mid",
                "Men's Shoes",
                "2 Colors",
                "US$125")
        )

        val adapter = WishlistProductAdapter(
            productList = wishlistProductList,
            onItemClicked = {product ->})


        binding.wishlistRcv.adapter = adapter
        binding.wishlistRcv.layoutManager = GridLayoutManager(requireContext(),2)
    }

}