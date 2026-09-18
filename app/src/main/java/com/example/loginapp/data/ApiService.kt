package com.example.loginapp.data

import com.example.loginapp.model.LoginRequest
import com.example.loginapp.model.LoginResponse
import com.example.loginapp.model.ProductResponse
import com.example.loginapp.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserResponse

    @GET("users")
    suspend fun getAllUsers(): List<UserResponse>

    // ===== US03: Catálogo general =====
    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

    // ===== US04: Filtro por categoría =====
    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductResponse>

    // ===== US05: Detalle del producto =====
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductResponse
}