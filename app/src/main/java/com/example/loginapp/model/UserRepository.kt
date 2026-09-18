package com.example.loginapp.model

import com.example.loginapp.data.RetrofitInstance
import retrofit2.HttpException

class UserRepository {

    suspend fun login(username: String, password: String): User {
        val request = LoginRequest(username, password)
        RetrofitInstance.api.login(request)

        val allUsers = RetrofitInstance.api.getAllUsers()
        val userResponse = allUsers.find { it.username == username }
            ?: throw IllegalStateException("Usuario no encontrado")

        return User(
            id = userResponse.id,
            username = userResponse.username,
            email = userResponse.email,
            firstName = userResponse.name?.firstname ?: "",
            lastName = userResponse.name?.lastname ?: "",
            phone = userResponse.phone ?: ""
        )
    }

    fun getRole(user: User): Role = Role.fromUserId(user.id)
}