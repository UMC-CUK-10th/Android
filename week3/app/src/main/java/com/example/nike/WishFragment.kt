package com.example.nike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope // 추가
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nike.databinding.FragmentWishBinding
import kotlinx.coroutines.launch // 추가

class WishFragment : Fragment() {
    private var _binding: FragmentWishBinding? = null
    private val binding get() = _binding!!

    // PreferenceManager 선언
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())

        // 1. DataStore에서 저장된 데이터를 실시간으로 관찰(Collect)합니다.
        viewLifecycleOwner.lifecycleScope.launch {
            preferenceManager.wishListFlow.collect { savedList ->
                // 2. 하트가 눌린(isFavorite == true) 상품만 필터링해서 보여줍니다.
                val favoriteList = savedList.filter { it.isFavorite }

                // 3. 어댑터 연결
                setupRecyclerView(ArrayList(favoriteList))
            }
        }
    }

    private fun setupRecyclerView(list: ArrayList<Product>) {
        val wishAdapter = ProductAdapter(list, isHome = false, isWish = true) { clickedProduct ->
            // 위시리스트에서 하트를 다시 누르면 삭제되는 로직 (선택사항)
            viewLifecycleOwner.lifecycleScope.launch {
                preferenceManager.saveWishList(list)
            }
        }
        binding.rvWish.apply {
            adapter = wishAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}