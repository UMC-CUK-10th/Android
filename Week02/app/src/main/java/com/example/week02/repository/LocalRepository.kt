package com.example.week02.repository

import com.example.week02.model.PurchaseProductData
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveProducts(productList: List<PurchaseProductData>)
    fun getProducts(): Flow<List<PurchaseProductData>>
}