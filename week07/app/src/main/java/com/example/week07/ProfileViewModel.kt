package com.example.week07

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

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
                // 🚀 최적화: 두 API를 병렬로 호출하여 전체 로딩 시간을 단축합니다.
                val profileDeferred = async { RetrofitClient.api.getUserProfile() }
                val listDeferred = async { RetrofitClient.api.getUserList() }

                // 두 요청이 모두 완료될 때까지 기다립니다.
                val profileResponse = profileDeferred.await()
                val listResponse = listDeferred.await()

                // 성공 상태에 두 데이터를 패키징하여 UI로 전달합니다.
                _uiState.value = ProfileUiState.Success(
                    myInfo = profileResponse.data,
                    followingList = listResponse.data
                )
            } catch (e: IOException) {
                // 네트워크 연결 오류 등 I/O 예외 처리
                _uiState.value = ProfileUiState.Error("네트워크 연결이 원활하지 않습니다.")
            } catch (e: Exception) {
                // 기타 일반적인 에러 처리
                _uiState.value = ProfileUiState.Error("통신 실패: ${e.localizedMessage}")
            }
        }
    }
}