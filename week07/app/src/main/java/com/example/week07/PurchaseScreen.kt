package com.example.week07

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PurchaseScreen(viewModel: SharedViewModel) {
    val tabs = listOf("전체", "TOPS & T-SHIRTS", "SALE")

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val wishedIds by viewModel.wishedIds.collectAsState()
    val allProducts = viewModel.allProducts

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 탭
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(title) }
                )
            }
        }

        // 스와이프 되는 화면 영역
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val displayProducts = if (page == 0) {
                allProducts
            } else {
                emptyList()
            }

            // ✨ 리스트가 비어있을 때(Tops & T-Shirts, SALE 탭) 보여줄 빈 화면 분기 처리
            if (displayProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("등록된 상품이 없습니다.", color = Color.Gray)
                }
            } else {
                // 전체 탭일 때 보여줄 상품 30개 그리드
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(items = displayProducts, key = { it.id }) { product ->
                        val isWished = wishedIds.contains(product.id)
                        ProductItem(
                            product = product.copy(isWished = isWished),
                            onHeartClick = { viewModel.toggleWishlist(product.id) }
                        )
                    }
                }
            }
        }
    }
}