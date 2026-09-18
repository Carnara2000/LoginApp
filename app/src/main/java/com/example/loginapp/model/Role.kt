package com.example.loginapp.model

enum class Role(val displayName: String) {
    ADMIN("Administrador"),
    AUDITOR("Auditor"),
    CLIENT("Cliente");

    companion object {
        fun fromUserId(id: Int): Role = when (id) {
            1, 2 -> ADMIN
            3 -> AUDITOR
            else -> CLIENT
        }
    }
}