package com.example.week02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week02.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newProductList = mutableListOf(
            HomeProductData(R.drawable.shoes1, "Air Jordan XXXVI", "US\$185"),
            HomeProductData(R.drawable.shoes2, "Nike Air Force 1 '07", "US\$115")
        )


        val adapter = HomeProductAdapter(newProductList, onItemClicked = { product -> })

        binding.homeRcv.adapter = adapter
        binding.homeRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }
}