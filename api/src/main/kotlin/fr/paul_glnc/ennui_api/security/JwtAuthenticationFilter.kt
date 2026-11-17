package fr.paul_glnc.ennui_api.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        // 1. Vérifie la présence du header Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)

        try {
            val userId = jwtService.extractUserId(jwt)

            // 2. Si l'email est extrait et qu'aucune authentification n'est encore positionnée dans le contexte
            if (SecurityContextHolder.getContext().authentication == null && !jwtService.isTokenExpired(jwt)) {

                // Récupération du rôle encodé dans le token (ou rechargé depuis la BDD)
                val role = jwtService.extractRole(jwt) // Assurez-vous d'avoir une méthode pour extraire le rôle dans JwtService
                val authorities = listOf(SimpleGrantedAuthority(role.name))

                val authToken = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                // 3. Authentification enregistrée dans le SecurityContext
                SecurityContextHolder.getContext().authentication = authToken
            }
        } catch (e: Exception) {
            // Jeton invalide ou expiré : on laisse passer pour que Spring Security gère le 401/403
        }

        filterChain.doFilter(request, response)
    }
}