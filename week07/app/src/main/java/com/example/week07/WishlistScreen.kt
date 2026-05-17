package com.example.week07

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week07.ProductData
import com.example.week07.R

val WishlistProductList = listOf(
    ProductData(1, R.drawable.socks1, "Nike Everyday Plus Cushioned","Training Ankle Socks(6 Pairs)","5 Colours","US/$10"),
    ProductData(2, R.drawable.shoes5,"Air Jordan 1 Mid","Men's Shoes","1 Colours","US/$125")
)
@Composable
fun WishlistScreen() {
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
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(
                items = WishlistProductList,
                key = { it.id }
            ) { product ->
                ProductItem(product)
            }
        }
    }
}