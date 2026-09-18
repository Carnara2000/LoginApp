package com.example.loginapp.controller

import com.example.loginapp.model.Role
import com.example.loginapp.model.User
import com.example.loginapp.model.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginController(
    private val repository: UserRepository = UserRepository()
) {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun onLoginSuccess(user: User, role: Role)
        fun onLoginError(message: String)
        fun onNoConnection()
    }

    fun doLogin(username: String, password: String, view: View) {
        if (username.isBlank() || password.isBlank()) {
            view.onLoginError("Completa todos los campos")
            return
        }

        // Aquí podrías verificar conectividad real con ConnectivityManager,
        // pero para simplificar, asumimos que si falla la petición, se maneja el error.

        view.showLoading()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = repository.login(username, password)
                val role = repository.getRole(user)

                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.onLoginSuccess(user, role)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    when (e.code()) {
                        401, 400 -> view.onLoginError("Usuario o contraseña inválidos")
                        else -> view.onLoginError("Error del servidor: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.onLoginError(e.message ?: "Error de conexión")
                }
            }
        }
    }
}