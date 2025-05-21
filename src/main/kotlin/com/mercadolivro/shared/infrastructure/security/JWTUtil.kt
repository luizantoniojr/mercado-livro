package com.mercadolivro.shared.security

import com.mercadolivro.shared.exception.AuthenticationException
import com.mercadolivro.shared.infrastructure.exception.Errors
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTUtil {

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    fun generateToken(subject: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration!!)
        val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))

        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun isValidToken(token: String): Boolean {
        return try {
            val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            claims.body.expiration.after(Date())
        } catch (e: Exception) {
            throw AuthenticationException(Errors.ML_003.message, Errors.ML_003.code)
        }
    }

    fun getSubjectFromToken(token: String): String {
        val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}