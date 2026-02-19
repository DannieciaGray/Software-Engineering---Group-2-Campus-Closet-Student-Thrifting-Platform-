package com.campuscloset.gsu.network

class SupabaseUserRepository(
    private val api: SupabaseApi
) {
    suspend fun emailExists(email: String): Boolean {
        val results = api.getUsersByEmail(emailEq = "eq.$email")
        return results.isNotEmpty()
    }

    suspend fun register(name: String, email: String, password: String): UserDto {
        val inserted = api.insertUser(
            UserDto(
                name = name,
                email = email,
                passwordHash = password, // (hash later)
                role = "USER"
            )
        )
        return inserted.first()
    }

    suspend fun login(email: String, password: String): UserDto? {
        val results = api.login(
            emailEq = "eq.$email",
            passEq = "eq.$password"
        )
        return results.firstOrNull()
    }

    suspend fun getUserById(userId: Int): UserDto? {
        val results = api.getUserById(idEq = "eq.$userId")
        return results.firstOrNull()
    }
}
