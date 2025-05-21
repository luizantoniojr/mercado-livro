package com.mercadolivro.shared.security

import com.mercadolivro.customer.CustomerStatus
import com.mercadolivro.customer.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    private val customer: CustomerModel
) : UserDetails {

    var id = customer.id
        private set

    override fun getAuthorities(): Collection<GrantedAuthority> =
        customer.roles.map { SimpleGrantedAuthority(it.description) }

    override fun getPassword(): String = customer.password
    override fun getUsername(): String = customer.email
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled(): Boolean = customer.status == CustomerStatus.ACTIVE
}