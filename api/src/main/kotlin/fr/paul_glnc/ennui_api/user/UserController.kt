package fr.paul_glnc.ennui_api.user

import fr.paul_glnc.ennui_api.user.dto.AuthResponse
import fr.paul_glnc.ennui_api.user.dto.CreateUserRequest
import fr.paul_glnc.ennui_api.user.dto.LoginRequest
import fr.paul_glnc.ennui_api.user.dto.UserResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody request: CreateUserRequest): UserResponse {
        return userService.register(request)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody request: LoginRequest): AuthResponse {
        return userService.login(request)
    }

    @PatchMapping("/{id}/promote")
    @ResponseStatus(HttpStatus.OK)
    fun promote(@PathVariable id: Long) : UserResponse {
        return userService.promoteToAdmin(id)
    }

    // Utilisé aussi bien pour l'auto-suppression que pour la suppression par un Admin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(
        @PathVariable id: Long,
        @AuthenticationPrincipal currentUserId: String // Spring Security injecte l'email du token JWT
    ) {
        userService.deleteUser(id, currentUserId)
    }
}