package com.example.week07

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week07.ProductData
import com.example.week07.R
@Composable
fun HomeScreen() {

    val productList = listOf(
        ProductData(1, R.drawable.shoes1,"Air Jordan XXXVI","","","US/$185"),
        ProductData(2,R.drawable.shoes2,"Nike Air Force1'08","","","US/$115")
    )

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))

            Text(text="Discover",fontSize=32.sp, fontWeight = FontWeight.Bold)
            Text("9월 4일 목요일",fontSize = 14.sp,fontWeight = FontWeight.Bold,color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id=R.drawable.image_homelogo),
                contentDescription = "배너이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 16.dp)
            )

            Text(text="What's new",fontSize = 18.sp,fontWeight = FontWeight.Bold)
            Text(text="나이키 최신 상품",fontSize=28.sp, fontWeight = FontWeight.Bold,color =Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow{
                items(items=productList, key={it.id}) {product->
                    ProductItem(product=product)
                }
            }
        }
    }
}