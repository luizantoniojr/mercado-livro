package com.mercadolivro.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
annotation class UserCanOnlyAccessTheirOwnResource