package com.example.loginapp.model

import android.content.Context

/**
 * Guarda la sesión del usuario en SharedPreferences.
 * US05: el rol debe leerse de aquí, NO de la API.
 */
class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveSession(token: String, role: Role, userId: Int, username: String) {
        prefs.edit()
            .putString("TOKEN", token)
            .putString("ROLE", role.name)       // guarda "ADMIN", "AUDITOR", "CLIENT"
            .putInt("USER_ID", userId)
            .putString("USERNAME", username)
            .apply()
    }

    fun getRole(): Role {
        val roleName = prefs.getString("ROLE", Role.CLIENT.name) ?: Role.CLIENT.name
        return try {
            Role.valueOf(roleName)
        } catch (e: Exception) {
            Role.CLIENT                          // fallback seguro
        }
    }

    fun getToken(): String {
        return prefs.getString("TOKEN", "") ?: ""
    }

    fun getUserId(): Int {
        return prefs.getInt("USER_ID", 0)
    }

    fun getUsername(): String {
        return prefs.getString("USERNAME", "") ?: ""
    }

    fun isAdmin(): Boolean {
        return getRole() == Role.ADMIN
    }

    fun isLoggedIn(): Boolean {
        return getToken().isNotEmpty() || getUsername().isNotEmpty()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}