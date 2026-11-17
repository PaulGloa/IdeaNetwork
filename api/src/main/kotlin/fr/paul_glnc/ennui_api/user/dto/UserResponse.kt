package fr.paul_glnc.ennui_api.user.dto

import fr.paul_glnc.ennui_api.user.Role

data class UserResponse(
    val id: Long,
    val email: String,
    val role: Role
)