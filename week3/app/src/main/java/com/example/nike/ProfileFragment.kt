package com.example.nike

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nike.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // ✅ 핵심: 따로 만든 RetrofitClient 공장을 연결합니다.
    private val apiService = RetrofitClient.apiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenus()

        // 데이터 로드 함수 실행
        loadUserProfile()
        loadFollowingList()
    }

    private fun setupMenus() {
        binding.menuOrder.apply {
            tvMenuName.text = "주문"
            ivMenuIcon.setImageResource(R.drawable.archive)
        }
        binding.menuPass.apply {
            tvMenuName.text = "패스"
            ivMenuIcon.setImageResource(R.drawable.identificationcard)
        }
        binding.menuEvent.apply {
            tvMenuName.text = "이벤트"
            ivMenuIcon.setImageResource(R.drawable.calendarblank)
        }
        binding.menuSetting.apply {
            tvMenuName.text = "설정"
            ivMenuIcon.setImageResource(R.drawable.gear)
        }
    }

    private fun loadUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 1번 유저 정보 가져오기
                val response = apiService.getUser(1)
                val user = response.data

                // UI 업데이트 (닉네임 합치기)
                binding.tvNickname.text = "${user.first_name} ${user.last_name}"

                // 이미지 로드
                Glide.with(this@ProfileFragment)
                    .load(user.avatar)
                    .circleCrop()
                    .into(binding.ivProfile)

                Log.d("API_SUCCESS", "1번 유저 로드 완료: ${user.first_name}")

            } catch (e: Exception) {
                Log.e("API_ERROR", "유저 정보 로드 실패: ${e.message}")
            }
        }
    }

    private fun loadFollowingList() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 유저 리스트 가져오기
                val response = apiService.getUsers()
                val followingAdapter = FollowingAdapter(response.data)

                binding.rvFollowing.apply {
                    adapter = followingAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }

                binding.tvFollowingTitle.text = "팔로잉 (${response.data.size})"
                Log.d("API_SUCCESS", "팔로잉 리스트 로드 완료: ${response.data.size}명")

            } catch (e: Exception) {
                Log.e("API_ERROR", "리스트 로드 실패: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}