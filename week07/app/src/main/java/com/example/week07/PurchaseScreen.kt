package com.example.week07

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week07.ProductData
import com.example.week07.R

val PurchaseProductList = listOf(
    ProductData(1, R.drawable.socks1, "Nike Elite Crew", "Basketball Socks","7 Colours","US/$16",true,true),
    ProductData(2,R.drawable.socks1,"Nike Everyday Plus Cushioned","Training Ankle Socks(6 Pairs)","5 Colours","US/$10",false,false),
    ProductData(3,R.drawable.shoes3,"Nike Air Force 1'07","Women's Shoes","5 Colours","US/$115",true,false),
    ProductData(4,R.drawable.shoes4,"Jordan ENike Air Force 1'07ssentials","Men's Shoes","2 Colours","US/$115",true,true)
)

@Composable
fun PurchaseScreen() {
    // ✨ 1. 클릭 시 화면이 새로 고쳐지도록 리스트를 Compose '상태(State)' 리스트로 변환
    val productList = remember { PurchaseProductList.toMutableStateList() }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 20.dp)
    ) {
        Text(
            text = "위시리스트",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            // 피그마 디자인처럼 아이템 사이 간격 조절
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = productList, // ✨ 원본 대신 상태 리스트를 넣어줍니다.
                key = { it.id }
            ) { product ->

                // ✨ 2. ProductItem을 호출할 때 람다 함수(onHeartClick)를 직접 채워줍니다!
                ProductItem(
                    product = product,
                    onHeartClick = { clickedProduct ->
                        // 하트 클릭 시 변수(isWished)를 반전시켜 화면을 갱신하는 로직
                        val index = productList.indexOf(clickedProduct)
                        if (index != -1) {
                            productList[index] = clickedProduct.copy(isWished = !clickedProduct.isWished)
                        }
                    }
                )

            }
        }
    }
}