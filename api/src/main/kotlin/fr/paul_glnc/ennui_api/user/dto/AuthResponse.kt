package fr.paul_glnc.ennui_api.user.dto

data class AuthResponse(
    val token: String,
    val user: UserResponse
)