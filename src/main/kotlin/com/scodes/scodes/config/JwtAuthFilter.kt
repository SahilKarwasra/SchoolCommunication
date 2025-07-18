package com.scodes.scodes.config

import com.scodes.scodes.repository.UserRepository
import com.scodes.scodes.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")
    }


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization") ?: return filterChain.doFilter(request, response)
        if (!authHeader.startsWith("Bearer ")) return filterChain.doFilter(request, response)

        val token = authHeader.removePrefix("Bearer ")
        val username = jwtService.extractUsername(token)
        val user = userRepository.findByEmail(username) ?: return

        val auth =
            UsernamePasswordAuthenticationToken(user.email, null, listOf(SimpleGrantedAuthority("ROLE_${user.role}")))
        SecurityContextHolder.getContext().authentication = auth

        filterChain.doFilter(request, response)
    }
}