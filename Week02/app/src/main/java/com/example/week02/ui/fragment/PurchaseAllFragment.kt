package com.example.week02.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week02.repository.ProductDataManager
import com.example.week02.ui.adapter.PurchaseProductAdapter
import com.example.week02.model.PurchaseProductData
import com.example.week02.R
import com.example.week02.databinding.FragmentPurchaseAllBinding
import kotlinx.coroutines.launch

class PurchaseAllFragment : Fragment() {

    lateinit var binding: FragmentPurchaseAllBinding
    private lateinit var dataManager: ProductDataManager
    private var currentProductList: List<PurchaseProductData> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataManager = ProductDataManager(requireContext())

        val adapter = PurchaseProductAdapter(
            productList = emptyList(),
            onItemClicked = {},
            onHeartClicked = { clickedProduct ->
                lifecycleScope.launch {
                    clickedProduct.isWished = !clickedProduct.isWished
                    dataManager.saveProducts(currentProductList)
                }
            }
        )

        lifecycleScope.launch {
            dataManager.getProducts().collect { savedList ->
                if(savedList.isEmpty()){
                    val initialData = List(30){index ->
                        PurchaseProductData(
                            image = if (index % 2 == 0) R.drawable.shoes3 else R.drawable.socks1,
                            name = "Nike test product ${index + 1}",
                            subtitle = "test subtitle",
                            colors = "${(index % 5) + 1} Colors",
                            price = "US$${100 + index}",
                            isBestSeller = index % 3 == 0,
                            isWished = false
                        )
                    }
                    currentProductList = initialData
                    dataManager.saveProducts(initialData)
                }else {
                    currentProductList = savedList
                }
                val adapter = PurchaseProductAdapter(
                    productList = currentProductList,
                    onItemClicked = { product -> },
                    onHeartClicked = {
                        lifecycleScope.launch {
                            dataManager.saveProducts((currentProductList))
                        }
                    }
                )
                binding.purchaseAllRcv.adapter = adapter
                binding.purchaseAllRcv.layoutManager = GridLayoutManager(requireContext(),2)
            }
        }
    }

}