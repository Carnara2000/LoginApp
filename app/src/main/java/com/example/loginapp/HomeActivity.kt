package com.example.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Recibir datos del Intent
        val role = intent.getStringExtra("ROLE") ?: "Usuario"
        val userId = intent.getIntExtra("USER_ID", 0)
        val email = intent.getStringExtra("USER_EMAIL") ?: "No disponible"
        val username = intent.getStringExtra("USER_USERNAME") ?: "No disponible"
        val firstName = intent.getStringExtra("USER_FIRSTNAME") ?: ""
        val lastName = intent.getStringExtra("USER_LASTNAME") ?: ""
        val phone = intent.getStringExtra("USER_PHONE") ?: "No disponible"

        val fullName = if (firstName.isNotEmpty() || lastName.isNotEmpty()) {
            "$firstName $lastName".trim()
        } else {
            "No disponible"
        }

        // Referencias a los TextView
        val welcomeText = findViewById<TextView>(R.id.welcome_text)
        val roleText = findViewById<TextView>(R.id.role_text)
        val userIdText = findViewById<TextView>(R.id.user_id_text)
        val usernameText = findViewById<TextView>(R.id.username_text)
        val emailText = findViewById<TextView>(R.id.email_text)
        val nameText = findViewById<TextView>(R.id.name_text)
        val phoneText = findViewById<TextView>(R.id.phone_text)

        // Asignar valores
        welcomeText.text = "¡Bienvenido, $username!"
        roleText.text = "Rol: $role"
        userIdText.text = "ID: $userId"
        usernameText.text = "Usuario: $username"
        emailText.text = "Email: $email"
        nameText.text = "Nombre completo: $fullName"
        phoneText.text = "Teléfono: $phone"

        // Botón de cerrar sesión
        val logoutBtn = findViewById<Button>(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            finish() // Vuelve a la pantalla de login
        }
    }
}