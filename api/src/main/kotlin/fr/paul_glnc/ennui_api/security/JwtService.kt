package fr.paul_glnc.ennui_api.security

import fr.paul_glnc.ennui_api.user.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    // On injecte une clé secrète depuis application.properties / application.yml
    @Value("\${application.security.jwt.secret-key:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private val secretKeyString: String,

    // Durée de validité du token (ex: 24h en millisecondes)
    @Value("\${application.security.jwt.expiration:86400000}")
    private val jwtExpiration: Long
) {

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    }

    // 1. Génération du token avec le sujet (userId) et les claims personnalisés (role)
    fun generateToken(userId : Long, role: Role): String {
        val extraClaims = mapOf("role" to role.name)

        return Jwts.builder()
            .claims(extraClaims)
            .subject(userId.toString())
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(secretKey)
            .compact()
    }

    // 2. Extraction du userId (Subject)
    fun extractUserId(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractRole(token: String): Role {
        val claims = extractAllClaims(token)
        // Récupère la valeur sous la clé "role" et la cast en String
        val roleString = claims["role", String::class.java]
            ?: throw IllegalArgumentException("Le claim 'role' est absent du token")

        // Convertit la chaîne (ex: "ROLE_USER") vers la valeur d'enum correspondante
        return Role.valueOf(roleString)
    }

    // 3. Validation de l'expiration du token
    fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }

    // Méthode utilitaire pour parser et vérifier la signature du token
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}