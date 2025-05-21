package com.mercadolivro.shared.exception

import org.springframework.security.core.AuthenticationException

class AuthenticationException(override val message: String, val errorCode: String) : AuthenticationException(message) {
}