package com.example.week02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week02.databinding.FragmentPurchaseBinding

class PurchaseFragment : Fragment() {

    lateinit var binding: FragmentPurchaseBinding

    private val purchaseProductList = List(30){index ->
        PurchaseProductData(
            image = if (index%2==0) R.drawable.shoes3 else R.drawable.socks1,
            name = "Nike test product ${index + 1}",
            subtitle = "test subtitle",
            colors = "${(index % 5) + 1} Colors",
            price = "US$${100 + index}",
            isBestSeller = index % 3 == 0,
            isWished = false
        )
    }

    private val adapter by lazy {
        PurchaseProductAdapter(
            productList = purchaseProductList,
            onItemClicked = {product -> }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val purchaseProductList = mutableListOf(
            PurchaseProductData(R.drawable.socks1,
                "Nike Everyday Plus Cushioned",
                "Training Ankle Socks(6 Pairs)",
                "5 Colors",
                "US$10",
                isBestSeller = true),
            PurchaseProductData(R.drawable.socks2,
                "Nike Elite Crew",
                "Basketball Socks",
                "7 Colors",
                "US$16",
                isBestSeller = false),
            PurchaseProductData(R.drawable.shoes3,
                "Nike Air Force 1 '07",
                "Women's Shoes",
                "5 Colors",
                "US$115",
                isBestSeller = false),
            PurchaseProductData(R.drawable.shoes4,
                "Jordan ENike Air Force 1'07ssentials",
                "Men's Shoes",
                "2 Colors",
                "US$115",
                isBestSeller = true)
            )*/

        binding.purchaseRcv.adapter = adapter
        binding.purchaseRcv.layoutManager = GridLayoutManager(requireContext(),2)


    }

}