package com.example.loginapp

data class UserResponse(
    val id: Int,
    val email: String,
    val username: String,
    val name: Name?,
    val phone: String?
)

data class Name(
    val firstname: String,
    val lastname: String
)