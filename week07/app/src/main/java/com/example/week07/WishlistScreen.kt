package com.example.week07

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WishlistScreen(viewModel: SharedViewModel) { // ✨ 뷰모델 받아오기
    val wishedIds by viewModel.wishedIds.collectAsState()

    // ✨ 전체 30개 상품 중, 찜(하트)을 누른 ID들만 걸러냅니다!
    val wishlistProducts = viewModel.allProducts.filter { wishedIds.contains(it.id) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "위시리스트",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(items = wishlistProducts, key = { it.id }) { product ->
                ProductItem(
                    product = product.copy(isWished = true), // 위시리스트니까 무조건 하트 켜짐
                    onHeartClick = { viewModel.toggleWishlist(product.id) } // 한번 더 누르면 찜 해제!
                )
            }
        }
    }
}