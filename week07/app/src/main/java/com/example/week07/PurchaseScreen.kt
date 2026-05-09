package com.example.week07

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun PurchaseScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("전체", "Tops & T-Shirts", "Shoes")

    Column (modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {selectedTab= index},
                    text = {Text(text=title)}
                )
            }
        }

        when(selectedTab) {
            0->PurchaseAllContent()
            1->PurchaseTopContent()
            2->PurchaseShoesContent()
        }
    }
}

@Composable
fun PurchaseAllContent() {
    Box (modifier = Modifier.fillMaxSize())
}

@Composable
fun PurchaseTopContent() {
    Box(modifier = Modifier.fillMaxSize())
}

@Composable
fun PurchaseShoesContent() {
    Box(modifier = Modifier.fillMaxSize())
}