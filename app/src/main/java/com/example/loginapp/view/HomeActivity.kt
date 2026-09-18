package com.example.loginapp.view
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.R
import com.example.loginapp.controller.HomeController
import com.example.loginapp.model.Role
import com.example.loginapp.model.User

class HomeActivity : AppCompatActivity(), HomeController.View {

    private val controller = HomeController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val user = User(
            id = intent.getIntExtra("USER_ID", 0),
            username = intent.getStringExtra("USER_USERNAME") ?: "",
            email = intent.getStringExtra("USER_EMAIL") ?: "",
            firstName = intent.getStringExtra("USER_FIRSTNAME") ?: "",
            lastName = intent.getStringExtra("USER_LASTNAME") ?: "",
            phone = intent.getStringExtra("USER_PHONE") ?: ""
        )

        val roleName = intent.getStringExtra("ROLE") ?: "Cliente"
        val role = Role.values().firstOrNull { it.displayName == roleName } ?: Role.CLIENT

        controller.loadUserData(user, role, this)

        findViewById<Button>(R.id.logout_btn).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btn_go_catalog).setOnClickListener {
            startActivity(Intent(this, CatalogActivity::class.java))
        }
    }

    override fun showUserData(user: User, role: Role) {
        findViewById<TextView>(R.id.welcome_text).text = "¡Bienvenido, ${user.username}!"
        findViewById<TextView>(R.id.user_name).text = "Nombre: ${user.fullName}"
        findViewById<TextView>(R.id.user_username).text = "Usuario: ${user.username}"
        findViewById<TextView>(R.id.user_email).text = "Email: ${user.email}"
        findViewById<TextView>(R.id.user_phone).text = "Teléfono: ${user.phone}"
        findViewById<TextView>(R.id.role_text).text = "Rol: ${role.displayName}"
        findViewById<TextView>(R.id.user_id_text).text = "Rol: ${user.id}"
    }
}