package com.example.nike


data class Product(
    val name: String,
    val category: String,
    val colors: String,
    val price: String,
    val imageRes: Int,
    val isBest: Boolean = false,
    var isFavorite: Boolean = false
)