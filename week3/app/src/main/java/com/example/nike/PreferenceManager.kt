package com.example.nike

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class PreferenceManager(private val context: Context) {
    private val gson = Gson()

    // ⭐ 키를 두 개로 분리했습니다.
    private val WISH_LIST_KEY = stringPreferencesKey("wish_list") // 구매하기(Top) 탭용
    private val HOME_LIST_KEY = stringPreferencesKey("home_list") // 홈 화면용

    // --- [1] 구매하기(TopFragment) 관련 로직 ---
    suspend fun saveWishList(productList: List<Product>) {
        val jsonString = gson.toJson(productList)
        context.dataStore.edit { prefs -> prefs[WISH_LIST_KEY] = jsonString }
    }

    val wishListFlow: Flow<List<Product>> = context.dataStore.data.map { prefs ->
        val jsonString = prefs[WISH_LIST_KEY] ?: ""
        if (jsonString.isEmpty()) emptyList() else gson.fromJson(jsonString, Array<Product>::class.java).toList()
    }

    // --- [2] 홈 화면(HomeFragment) 관련 로직 추가 ---
    suspend fun saveHomeList(productList: List<Product>) {
        val jsonString = gson.toJson(productList)
        context.dataStore.edit { prefs -> prefs[HOME_LIST_KEY] = jsonString }
    }

    val homeListFlow: Flow<List<Product>> = context.dataStore.data.map { prefs ->
        val jsonString = prefs[HOME_LIST_KEY] ?: ""
        if (jsonString.isEmpty()) emptyList() else gson.fromJson(jsonString, Array<Product>::class.java).toList()
    }
}