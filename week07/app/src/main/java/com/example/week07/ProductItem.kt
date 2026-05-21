package com.example.week07

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week07.ProductData

@Composable
fun ProductItem(product: ProductData, onHeartClick: ((ProductData)->Unit)?=null) {
    Column(
        modifier = Modifier
            .width(250.dp)
            .padding(end = 16.dp)
    ) {
        Box{
            Image (
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

            if(onHeartClick!=null) {
                IconButton(
                    onClick = {onHeartClick(product)},
                    modifier = Modifier.align(Alignment.TopEnd)
                ){
                    Image(
                        painter=painterResource(
                            id = if(product.isWished) R.drawable.heart
                            else R.drawable.blankheart
                        ),
                        contentDescription = "하트",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        if (product.isBestSeller) {
            Text(
                text = "BestSeller",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF543A) // 피그마와 비슷한 오렌지/다홍색 포인트 컬러
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = product.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        if(product.subtitle.isNotEmpty()) {
            Text(
                text=product.subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Text(
            text = product.price,
            fontSize = 13.sp
        )
    }
}