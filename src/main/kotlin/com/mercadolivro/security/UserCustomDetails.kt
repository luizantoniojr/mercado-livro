package com.mercadolivro.security

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    private val customer: CustomerModel
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        customer.roles.map { SimpleGrantedAuthority(it.description) }

    override fun getPassword(): String = customer.password
    override fun getUsername(): String = customer.email
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled(): Boolean = customer.status == CustomerStatus.ACTIVE
    fun getId() = customer.id
    fun getCustomer() = customer
}