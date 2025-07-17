package com.scodes.scodes.dto

import com.scodes.scodes.entity.Role

data class RegisterRequest(
    val email: String,
    val password: String,
    val role: Role
)