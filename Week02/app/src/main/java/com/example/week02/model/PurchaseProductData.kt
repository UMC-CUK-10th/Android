package com.example.week02.model

data class PurchaseProductData (
    val image: Int,
    val name: String,
    val subtitle: String,
    val colors: String,
    val price: String,
    val isBestSeller: Boolean = false,
    var isWished: Boolean = false
)