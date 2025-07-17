package com.scodes.scodes.service

import com.scodes.scodes.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService{

    private val secretKey = "SecretKey"

    fun generateToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.email)
            .claim("role", user.role.name)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()), SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUsername (token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .build()
            .parseClaimsJwt(token)
            .body
            .subject
}