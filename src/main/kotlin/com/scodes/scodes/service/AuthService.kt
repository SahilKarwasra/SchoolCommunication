package com.scodes.scodes.service

import com.scodes.scodes.dto.AuthResponse
import com.scodes.scodes.dto.LoginRequest
import com.scodes.scodes.dto.RegisterRequest
import com.scodes.scodes.entity.User
import com.scodes.scodes.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService (
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register (request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email))
            throw RuntimeException("Email Already Exists")

        val user = userRepository.save(
            User(email = request.email, password = passwordEncoder.encode(request.password), role = request.role)
        )

        val token = jwtService.generateToken(user)
        return AuthResponse(token)
    }

    fun login (request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw RuntimeException("User Not Found")

        if (!passwordEncoder.matches(request.password, user.password))
            throw RuntimeException("Invalid Credentials")

        val token = jwtService.generateToken(user)
        return AuthResponse(token)
    }
}