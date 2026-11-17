package fr.paul_glnc.ennui_api.user

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.ROLE_USER
)