package com.example.loginapp.model

import com.example.loginapp.data.RetrofitInstance

class ProductRepository {

    // ===== US03 =====
    suspend fun getAllProducts(): List<Product> {
        return RetrofitInstance.api.getProducts().map { it.toDomain() }
    }

    // ===== US04 =====
    suspend fun getCategories(): List<String> {
        return RetrofitInstance.api.getCategories()
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        return RetrofitInstance.api.getProductsByCategory(category).map { it.toDomain() }
    }

    // ===== US05 =====
    suspend fun getProductById(id: Int): Product {
        return RetrofitInstance.api.getProductById(id).toDomain()
    }

    // Mapper: de respuesta de API → modelo de negocio
    private fun ProductResponse.toDomain(): Product = Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image
    )
}