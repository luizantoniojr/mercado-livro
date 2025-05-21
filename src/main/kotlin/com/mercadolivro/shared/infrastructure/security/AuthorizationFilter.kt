package com.mercadolivro.shared.security

import com.mercadolivro.shared.exception.AuthenticationException
import com.mercadolivro.shared.infrastructure.exception.Errors
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class AuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
    private val userDetailsService: UserDetailsService
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            val token = authHeader.split(" ")[1]
            if (token.isEmpty() || !jwtUtil.isValidToken(token))
                throw AuthenticationException(Errors.ML_004.message, Errors.ML_004.code)

            val sub = jwtUtil.getSubjectFromToken(token)
            val user = userDetailsService.loadUserByUsername(sub)

            val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain.doFilter(request, response)
    }
}