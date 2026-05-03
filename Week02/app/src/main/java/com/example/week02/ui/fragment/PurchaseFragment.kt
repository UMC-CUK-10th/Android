package com.example.week02.ui.fragment // 패키지명은 본인 프로젝트에 맞게 확인해주세요!

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.week02.databinding.FragmentPurchaseBinding
import com.example.week02.ui.adapter.PurchaseViewPagerAdapter
import com.example.week02.viewmodel.PurchaseViewModel // ViewModel 패키지
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint // 📍 1. Hilt 적용을 위해 가장 먼저 추가해야 합니다!
class PurchaseFragment : Fragment() {

    // 📍 2. 메모리 누수를 막기 위한 안전한 바인딩 객체 선언 방식
    private var _binding: FragmentPurchaseBinding? = null
    private val binding get() = _binding!!

    // 📍 3. Hilt를 통한 ViewModel 주입
    private val purchaseViewModel: PurchaseViewModel by viewModels()

    private val tabTitles = arrayOf("전체", "Top & T-shirts", "Sale")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 기존 UI (뷰페이저 & 탭 레이아웃) 설정
        setupViewPager()

        // 2. ViewModel의 StateFlow 데이터 관찰 시작
        observeViewModel()

        // 3. 뷰모델에 초기 데이터 로드 요청
        purchaseViewModel.loadProducts()
    }

    private fun setupViewPager() {
        // 기존에 작성하신 ViewPager + TabLayout 코드를 그대로 분리해서 가져왔습니다.
        val adapter = PurchaseViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position] // 각 탭에 이름표 달아주기
        }.attach()
    }

    private fun observeViewModel() {
        // Fragment의 뷰 생명주기에 맞춰 코루틴 실행
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 1. 상품 리스트 상태 관찰 (StateFlow)
                launch {
                    purchaseViewModel.productList.collect { products ->
                        if (products.isNotEmpty()) {
                            // 💡 부모 프래그먼트에서 데이터를 받았으므로,
                            // 이제 이 데이터를 ViewPager 안의 자식 프래그먼트들로 넘겨주거나,
                            // 혹은 자식 프래그먼트들이 직접 ViewModel을 공유해서 사용하게 할 수 있습니다!
                        }
                    }
                }

                // 2. 에러 메시지 관찰 (SharedFlow)
                launch {
                    purchaseViewModel.errorMessage.collect { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 📍 4. Fragment가 화면에서 사라질 때 binding을 비워주어 메모리 누수 방지
        _binding = null
    }
}