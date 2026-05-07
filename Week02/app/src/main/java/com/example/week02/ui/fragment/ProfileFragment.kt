package com.example.week02.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.week02.R
import com.example.week02.ui.adapter.UserAdapter // 경로 확인 필요[cite: 3]
import com.example.week02.databinding.FragmentProfileBinding
import com.example.week02.viewmodel.ProfileViewModel // 추가된 ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint // 📍 1. Hilt 의존성 주입을 위해 필수
class ProfileFragment : Fragment() {

    // 📍 2. 메모리 누수 방지를 위한 ViewBinding 안전 처리
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // 📍 3. ViewModel 주입
    private val profileViewModel: ProfileViewModel by viewModels()

    // 어댑터를 전역으로 빼서 데이터만 갱신할 수 있도록 변경[cite: 3]
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. UI 클릭 이벤트 및 리사이클러뷰 껍데기 세팅
        setupUI()

        // 2. ViewModel의 데이터 관찰 시작 (변경 사항이 있으면 UI 업데이트)
        observeViewModel()

        // 3. 뷰모델에 1번 유저 정보와 1페이지 팔로잉 리스트 요청
        profileViewModel.fetchUserProfile(1)
        profileViewModel.fetchUserList(1)
    }

    private fun setupUI() {
        // 기존 코드 유지: 프로필 수정 버튼 클릭 이벤트
        binding.button2.setOnClickListener {
            val nextFragment = EditProfileFragment()

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.main_fcv, nextFragment)
                addToBackStack(null)
                commit()
            }
        }

        // 리사이클러뷰 초기 세팅 (데이터는 아직 없는 상태)[cite: 3]
        userAdapter = UserAdapter(mutableListOf())
        binding.followingRcv.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun observeViewModel() {
        // 생명주기에 안전한 Flow 수집
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 💡 1. 내 정보 데이터가 들어왔을 때 UI 업데이트
                launch {
                    profileViewModel.userProfile.collect { user ->
                        user?.let {
                            binding.profileNickname.text = "${it.firstName} ${it.lastName}"

                            Glide.with(this@ProfileFragment)
                                .load(it.avatar)
                                .into(binding.profileImage)
                        }
                    }
                }

                // 💡 2. 팔로잉 리스트 데이터가 들어왔을 때 UI 업데이트
                launch {
                    profileViewModel.userList.collect { userList ->
                        if (userList.isNotEmpty()) {
                            binding.followingCountTv.text = "팔로잉 (${userList.size})"

                            // 매번 새 어댑터를 만들지 않고, 기존 어댑터의 데이터만 교체합니다.[cite: 3]
                            userAdapter.submitList(userList)
                        }
                    }
                }

                // 💡 3. API 통신 실패 시 에러 처리
                launch {
                    profileViewModel.errorMessage.collect { message ->
                        Log.e("API_ERROR", message)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 뷰가 파괴될 때 바인딩도 비워줍니다.
    }
}