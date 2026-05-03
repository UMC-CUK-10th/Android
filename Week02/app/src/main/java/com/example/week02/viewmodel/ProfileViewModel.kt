package com.example.week02.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week02.model.UserData // 패키지명은 프로젝트에 맞게 확인해주세요!
import com.example.week02.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository // 인터페이스 주입!
) : ViewModel() {

    // 1. 단일 유저 정보 상태 (초기값: null - 아직 데이터 없음)
    private val _userProfile = MutableStateFlow<UserData?>(null)
    val userProfile: StateFlow<UserData?> = _userProfile.asStateFlow()

    // 2. 유저 리스트 상태 (초기값: 빈 리스트)
    private val _userList = MutableStateFlow<List<UserData>>(emptyList())
    val userList: StateFlow<List<UserData>> = _userList.asStateFlow()

    // 3. 에러 메시지 이벤트 (토스트용)
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    // 로딩 상태 (선택 사항: 프로그레스 바를 보여주고 싶을 때 사용)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- 비즈니스 로직 (API 호출) ---

    // 1. 마이페이지 상단 내 정보 가져오기
    fun fetchUserProfile(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = remoteRepository.getUserProfile(userId)

            result.onSuccess { response ->
                _userProfile.value = response.data // 데이터 갱신
            }.onFailure { exception ->
                _errorMessage.emit("내 정보 불러오기 실패: ${exception.message}")
            }
            _isLoading.value = false
        }
    }

    // 2. 마이페이지 하단 팔로잉 리스트 가져오기
    fun fetchUserList(page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = remoteRepository.getUserList(page)

            result.onSuccess { response ->
                _userList.value = response.data // 리스트 갱신
            }.onFailure { exception ->
                _errorMessage.emit("유저 리스트 불러오기 실패: ${exception.message}")
            }
            _isLoading.value = false
        }
    }
}