package com.example.week07

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ✨ 에러의 원인 해결! 상태 클래스가 myInfo와 followingList를 모두 가지도록 수정되었습니다.
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val myInfo: ReqresUser, val followingList: List<ReqresUser>) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                // 내 프로필 정보(1번 유저)와 팔로잉 리스트(1페이지) 데이터를 순서대로 가져옵니다.
                val profileResponse = RetrofitClient.api.getUserProfile()
                val listResponse = RetrofitClient.api.getUserList()

                // 성공 상태에 두 데이터를 예쁘게 포장해서 UI(ProfileScreen)로 넘겨줍니다.
                _uiState.value = ProfileUiState.Success(
                    myInfo = profileResponse.data,
                    followingList = listResponse.data
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("통신 실패: ${e.message}")
            }
        }
    }
}