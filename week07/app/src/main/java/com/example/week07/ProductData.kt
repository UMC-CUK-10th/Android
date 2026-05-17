package com.example.week07

data class ProductData (
    val id: Int,
    val image: Int,
    val name: String,
    val subtitle: String,
    val colors: String,
    val price: String,
    val isBestSeller: Boolean = false,
    var isWished: Boolean = false
)