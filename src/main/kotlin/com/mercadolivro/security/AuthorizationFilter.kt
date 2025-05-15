package com.mercadolivro.security

import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.AuthenticationException
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
        request: jakarta.servlet.http.HttpServletRequest,
        response: jakarta.servlet.http.HttpServletResponse,
        chain: jakarta.servlet.FilterChain
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