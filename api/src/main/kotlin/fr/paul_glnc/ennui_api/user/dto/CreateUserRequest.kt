package fr.paul_glnc.ennui_api.user.dto

data class CreateUserRequest(
    val email: String,
    val password: String
)