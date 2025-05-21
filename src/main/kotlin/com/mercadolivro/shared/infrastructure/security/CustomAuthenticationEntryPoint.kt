package com.mercadolivro.shared.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.response.ErrorResponse
import com.mercadolivro.shared.infrastructure.exception.Errors
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val error = ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            Errors.ML_002.message,
            Errors.ML_002.code,
            null
        )

        response.outputStream.print(jacksonObjectMapper().writeValueAsString(error))
    }
}