package com.example.loginapp.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
) {
    val formattedPrice: String
        get() = "$${"%.2f".format(price)}"
}