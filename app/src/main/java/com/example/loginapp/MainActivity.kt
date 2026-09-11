package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)

        loginBtn.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Usamos corrutinas para simular asincronía (aunque sea local)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // 1. Login (local)
                    val loginResponse = FakeApiService.login(LoginRequest(username, password))
                    val token = loginResponse.token

                    // 2. Obtener perfil (local)
                    val userResponse = FakeApiService.getUser(token)
                    val userId = userResponse.id

                    // 3. Mapeo de roles (según User Story)
                    val role = when (userId) {
                        1, 2 -> "Administrador"
                        3 -> "Auditor"
                        else -> "Cliente"
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Bienvenido $role (ID: $userId)",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(this@MainActivity, HomeActivity::class.java).apply {
                            putExtra("ROLE", role)
                            putExtra("USER_ID", userId)
                            putExtra("TOKEN", token)
                            // Agregamos todos los datos del usuario
                            putExtra("USER_EMAIL", userResponse.email)
                            putExtra("USER_USERNAME", userResponse.username)
                            putExtra("USER_FIRSTNAME", userResponse.name?.firstname ?: "")
                            putExtra("USER_LASTNAME", userResponse.name?.lastname ?: "")
                            putExtra("USER_PHONE", userResponse.phone ?: "")
                        }
                        startActivity(intent)
                        finish()
                    }

                } catch (e: FakeApiException) {
                    // Error de autenticación (Escenario 2)
                    withContext(Dispatchers.Main) {
                        when (e.code) {
                            401 -> Toast.makeText(
                                this@MainActivity,
                                "Usuario o contraseña inválidos",
                                Toast.LENGTH_SHORT
                            ).show()
                            else -> Toast.makeText(
                                this@MainActivity,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    // Otros errores
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}