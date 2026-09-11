package com.example.loginapp

// Clase que simula el comportamiento de la API sin necesidad de red
object FakeApiService {

    // Base de datos de usuarios predefinidos (IDs y roles según la historia)
    private val users = listOf(
        FakeUser(1, "johnd", "m38rmF$", "john@mail.com", "John", "Doe", "123456789"),
        FakeUser(2, "mor_2314", "83r5^_", "morrison@mail.com", "Morrison", "Lee", "987654321"),
        FakeUser(3, "kevinryan", "kev02937@", "kevin@mail.com", "Kevin", "Ryan", "456789123"),
        FakeUser(4, "dmitriyw", "dmitriyw", "dmitriy@mail.com", "Dmitriy", "W", "789123456"),
        FakeUser(5, "testuser", "testpass", "test@mail.com", "Test", "User", "111111111")
    )

    // Simula el endpoint /auth/login
    fun login(request: LoginRequest): LoginResponse {
        // Buscar usuario por username y password
        val user = users.find { it.username == request.username && it.password == request.password }
        return if (user != null) {
            // Si existe, devolver un token falso (usamos el ID para generar algo único)
            LoginResponse(token = "fake-jwt-token-${user.id}")
        } else {
            // Si no existe, lanzar excepción simulando error 401
            throw FakeApiException(401, "Usuario o contraseña inválidos")
        }
    }

    // Simula el endpoint /auth/user (requiere token)
    fun getUser(token: String): UserResponse {
        // Extraer el ID del token (formato: "fake-jwt-token-{id}")
        val userId = token.split("-").lastOrNull()?.toIntOrNull()
        val user = users.find { it.id == userId }
        return if (user != null) {
            UserResponse(
                id = user.id,
                email = user.email,
                username = user.username,
                name = Name(user.firstname, user.lastname),
                phone = user.phone
            )
        } else {
            throw FakeApiException(401, "Token inválido")
        }
    }

    // Función para simular pérdida de conectividad (opcional, para el escenario 3)
    fun isNetworkAvailable(): Boolean = true // Siempre true, pero puedes cambiarlo a false para probar
}

// Datos internos de usuario (no expuestos fuera de este archivo)
private data class FakeUser(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val phone: String
)

// Excepción personalizada para simular errores HTTP
class FakeApiException(val code: Int, message: String) : Exception(message)
