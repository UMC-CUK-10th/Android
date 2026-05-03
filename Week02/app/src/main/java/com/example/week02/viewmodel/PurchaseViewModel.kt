package com.example.week02.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week02.model.PurchaseProductData
import com.example.week02.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    // 1. 상태(State)를 보관하는 StateFlow (초기값: 빈 리스트)
    private val _productList = MutableStateFlow<List<PurchaseProductData>>(emptyList())
    val productList: StateFlow<List<PurchaseProductData>> = _productList.asStateFlow()

    // 2. 일회성 이벤트(Event)를 처리하는 SharedFlow (Toast 에러 메시지용)
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun loadProducts() {
        viewModelScope.launch {
            localRepository.getProducts()
                .catch { e ->
                    // 에러 발생 시 SharedFlow로 이벤트 방출 (emit)
                    _errorMessage.emit("데이터를 불러오는 데 실패했습니다: ${e.message}")
                }
                .collect { products ->
                    // 데이터가 들어오면 StateFlow의 value 갱신
                    _productList.value = products
                }
        }
    }

    fun saveProducts(products: List<PurchaseProductData>) {
        viewModelScope.launch {
            localRepository.saveProducts(products)
            loadProducts()
        }
    }
}