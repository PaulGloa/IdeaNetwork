package fr.paul_glnc.ennui_api.user.dto

data class LoginRequest(
    val email: String,
    val password: String
)