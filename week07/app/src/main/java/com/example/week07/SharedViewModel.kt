package com.example.week07

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ✨ 화면들끼리 데이터를 공유하고, 앱을 꺼도 하트를 기억하게 해주는 ViewModel
class SharedViewModel(application: Application) : AndroidViewModel(application) {

    // 안드로이드 기기 내부에 데이터를 영구 저장하는 도구 (SharedPreferences)
    private val prefs = application.getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE)

    // 1. 랜덤 상품 30개 생성해서 가지고 있기
    val allProducts = generateDummyProducts()

    // 2. 찜(하트) 누른 상품의 ID들을 기억하는 리스트 상태
    private val _wishedIds = MutableStateFlow<Set<Int>>(emptySet())
    val wishedIds: StateFlow<Set<Int>> = _wishedIds.asStateFlow()

    init {
        // 앱이 켜질 때, 기기에 저장되어 있던 찜 목록(ID들)을 싹 불러옵니다.
        val savedIds = prefs.getStringSet("wished_ids", emptySet()) ?: emptySet()
        _wishedIds.value = savedIds.mapNotNull { it.toIntOrNull() }.toSet()
    }

    // 3. 하트 버튼을 눌렀을 때 실행될 함수 (추가 / 삭제 토글)
    fun toggleWishlist(productId: Int) {
        val current = _wishedIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId) // 이미 있으면 찜 해제
        } else {
            current.add(productId) // 없으면 찜 추가
        }
        _wishedIds.value = current

        // 변경된 찜 목록을 기기에 영구 저장! (앱 껐다 켜도 유지됨)
        prefs.edit().putStringSet("wished_ids", current.map { it.toString() }.toSet()).apply()
    }

    // 30개의 가짜 데이터 생성 함수
    private fun generateDummyProducts(): List<ProductData> {
        val images = listOf(
            R.drawable.socks1, R.drawable.shoes1,
            R.drawable.shoes2, R.drawable.shoes3, R.drawable.shoes4
        )
        return (1..30).map { i ->
            ProductData(
                id = i,
                image = images[i % images.size], // 이미지 번갈아가며 넣기
                name = "Nike test product $i",
                subtitle = "test subtitle",
                colors = "${(1..5).random()} Colors",
                price = "US$${100 + i}",
                isBestSeller = i % 4 == 0 // 4번째마다 베스트셀러 딱지
            )
        }
    }
}