package com.scodes.scodes.controllers

import com.scodes.scodes.dto.AuthResponse
import com.scodes.scodes.dto.LoginRequest
import com.scodes.scodes.dto.RegisterRequest
import com.scodes.scodes.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController (
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register (@RequestBody request: RegisterRequest) : AuthResponse = authService.register(request)

    @PostMapping("/login")
    fun login (@RequestBody request: LoginRequest) : AuthResponse = authService.login(request)

    @GetMapping("/me")
    fun me(authentication: Authentication?): ResponseEntity<Any> {
        return if (authentication != null) {
            ResponseEntity.ok(authentication.name)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated")
        }
    }

}