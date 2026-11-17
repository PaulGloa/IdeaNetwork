package fr.paul_glnc.ennui_api.user

import fr.paul_glnc.ennui_api.user.dto.AuthResponse
import fr.paul_glnc.ennui_api.user.dto.CreateUserRequest
import fr.paul_glnc.ennui_api.user.dto.LoginRequest
import fr.paul_glnc.ennui_api.user.dto.UserResponse
import fr.paul_glnc.ennui_api.security.JwtService
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    // Inscription (Register)
    fun register(request: CreateUserRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Un utilisateur avec cet email existe déjà")
        }

        val newUser = User(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            role = Role.ROLE_USER
        )

        val savedUser = userRepository.save(newUser)
        return UserResponse(id = savedUser.id!!, email = savedUser.email, role = savedUser.role)
    }

    // Connexion (Login)
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Identifiants invalides")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw IllegalArgumentException("Identifiants invalides")
        }

        val token = jwtService.generateToken(user.id!!, user.role)

        return AuthResponse(
            token = token,
            user = UserResponse(id = user.id!!, email = user.email, role = user.role)
        )
    }

    // Passage d'un utilisateur en admin
    fun promoteToAdmin(userId: Long): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("Utilisateur avec l'ID $userId introuvable") }

        user.role = Role.ROLE_ADMIN

        val updatedUser = userRepository.save(user)
        return UserResponse(id = updatedUser.id!!, email = updatedUser.email, role = updatedUser.role)
    }

    // Suppression d'un compte par ID
    fun deleteUser(targetUserId: Long, currentUserIdString: String) {
        val currentUserId = currentUserIdString.toLong()
        val targetUser = userRepository.findById(targetUserId)
            .orElseThrow { NoSuchElementException("Utilisateur avec l'ID $targetUserId introuvable") }

        val currentUser = userRepository.findById(currentUserId)
            .orElseThrow { IllegalArgumentException("Utilisateur connecté introuvable") }

        // Règle de sécurité : Si ce n'est PAS un admin ET qu'il essaie de supprimer un autre ID que le sien
        if (currentUser.role != Role.ROLE_ADMIN && currentUser.id != targetUser.id) {
            throw AccessDeniedException("Vous n'avez pas le droit de supprimer ce compte")
        }

        userRepository.delete(targetUser)
    }
}