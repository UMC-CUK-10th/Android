package com.example.week02.repository

// 📍 ProductDataManager의 위치가 com.example.week02.ProductDataManager가 맞는지 임포트를 확인해 주세요!
import com.example.week02.repository.ProductDataManager
import com.example.week02.model.PurchaseProductData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    // 📍 변수명(productDataManager)과 타입(ProductDataManager)이 정확히 적혀 있어야 합니다.
    private val productDataManager: ProductDataManager
) : LocalRepository {

    override suspend fun saveProducts(productList: List<PurchaseProductData>) {
        productDataManager.saveProducts(productList)
    }

    override fun getProducts(): Flow<List<PurchaseProductData>> {
        return productDataManager.getProducts()
    }
}