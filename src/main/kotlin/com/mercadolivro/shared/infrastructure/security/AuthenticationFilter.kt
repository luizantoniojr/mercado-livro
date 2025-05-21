package com.mercadolivro.shared.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.request.LoginRequest
import com.mercadolivro.shared.exception.AuthenticationException
import com.mercadolivro.shared.infrastructure.exception.Errors
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class AuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {

        try {
            val loginRequest = jacksonObjectMapper().readValue(request.inputStream, LoginRequest::class.java)
            val authenticationToken = UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password,
            )
            return authenticationManager.authenticate(authenticationToken)
        } catch (_: Exception) {
            throw AuthenticationException(Errors.ML_002.message, Errors.ML_002.code)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val user = authResult.principal as UserCustomDetails
        val token = jwtUtil.generateToken(user.username)
        response.addHeader("Authorization", "Bearer $token")
    }
}