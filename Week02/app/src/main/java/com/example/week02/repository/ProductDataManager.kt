package com.example.week02.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.week02.model.PurchaseProductData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "product_pref")

class ProductDataManager(private val context: Context) {
    private val gson = Gson()

    private val PRODUCT_KEY = stringPreferencesKey("product_list")

    suspend fun saveProducts(productList: List<PurchaseProductData>){
        val jsonString = gson.toJson(productList)
        context.dataStore.edit { prefs ->
            prefs[PRODUCT_KEY] = jsonString
        }
    }

    fun getProducts(): Flow<List<PurchaseProductData>>{
        return context.dataStore.data.map { prefs ->
            val jsonString = prefs[PRODUCT_KEY]
            if (jsonString != null){
                try{
                    val type = object : TypeToken<List<PurchaseProductData>>(){}.type
                    gson.fromJson(jsonString, type)
                } catch(e: Exception){
                    emptyList()
                }

            }else{
                emptyList()
            }
        }
    }
}