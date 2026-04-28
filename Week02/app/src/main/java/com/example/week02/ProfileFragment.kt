package com.example.week02

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.week02.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    // 💡 1. 우리가 만든 Retrofit API 가져오기
    private val api = ApiClient.reqResService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 💡 2. 프로필 수정 버튼 클릭 이벤트 (작성해주신 코드 유지!)
        binding.button2.setOnClickListener {
            val nextFragment = EditProfileFragment()

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.main_fcv, nextFragment)
                // 뒤로 가기 버튼을 눌렀을 때 앱이 꺼지지 않고 이전 화면으로 돌아오도록 추가해 주면 좋습니다!
                addToBackStack(null)
                commit()
            }
        }

        // 💡 3. 서버에서 내 정보(1번 유저) 가져와서 띄우기
        fetchMyProfile()

        // 💡 4. 서버에서 팔로잉 리스트 가져와서 리사이클러뷰에 연결하기
        fetchFollowingList()
    }

    // --- 서버 통신 함수들 ---

    private fun fetchMyProfile() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = api.getUserProfile(1) // 1번 유저 요청
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    if (user != null) {
                        withContext(Dispatchers.Main) {
                            // 닉네임 세팅
                            binding.profileNickname.text = "${user.firstName} ${user.lastName}"

                            // 프로필 이미지 세팅 (XML에서 CardView로 감싸서 이미 둥글게 잘리므로 circleCrop()은 뺐습니다!)
                            Glide.with(this@ProfileFragment)
                                .load(user.avatar)
                                .into(binding.profileImage)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "내 정보 불러오기 실패: ${e.message}")
            }
        }
    }

    private fun fetchFollowingList() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = api.getUserList(1) // 유저 리스트 요청
                if (response.isSuccessful) {
                    val userList = response.body()?.data
                    if (userList != null) {
                        withContext(Dispatchers.Main) {
                            // "팔로잉 (개수)" 텍스트 업데이트
                            binding.followingCountTv.text = "팔로잉 (${userList.size})"

                            // 리사이클러뷰 어댑터 연결
                            val adapter = UserAdapter(userList.toMutableList())
                            binding.followingRcv.adapter = adapter
                            binding.followingRcv.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "팔로잉 리스트 불러오기 실패: ${e.message}")
            }
        }
    }
}