package com.example.loginapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.R
import com.example.loginapp.controller.LoginController
import com.example.loginapp.model.Role
import com.example.loginapp.model.SessionManager
import com.example.loginapp.model.User

class MainActivity : AppCompatActivity(), LoginController.View {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button

    private val controller = LoginController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)

        loginBtn.setOnClickListener {
            controller.doLogin(
                usernameInput.text.toString().trim(),
                passwordInput.text.toString().trim(),
                this
            )
        }
    }

    override fun showLoading() {
        loginBtn.isEnabled = false
        loginBtn.text = "Cargando..."
    }

    override fun hideLoading() {
        loginBtn.isEnabled = true
        loginBtn.text = "Iniciar Sesión"
    }

    override fun onLoginSuccess(user: User, role: Role) {
        // Guardar sesión localmente (US05 lo requiere)
        val session = SessionManager(this)
        session.saveSession(
            token = "",         // o el token real si lo pasas
            role = role,
            userId = user.id,
            username = user.username
        )

        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("USER_ID", user.id)
            putExtra("USER_USERNAME", user.username)
            putExtra("USER_EMAIL", user.email)
            putExtra("USER_FIRSTNAME", user.firstName)
            putExtra("USER_LASTNAME", user.lastName)
            putExtra("USER_PHONE", user.phone)
            putExtra("ROLE", role.displayName)
        }
        startActivity(intent)
        finish()
    }

    override fun onLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onNoConnection() {
        Toast.makeText(this, "Sin conexión a internet", Toast.LENGTH_SHORT).show()
    }
}