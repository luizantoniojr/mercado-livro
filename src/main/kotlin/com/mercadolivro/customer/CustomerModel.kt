package com.mercadolivro.customer

import com.mercadolivro.customer.response.CustomerResponse
import com.mercadolivro.shared.infrastructure.security.Role
import jakarta.persistence.*

@Entity(name = "customers")
data class CustomerModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    @Column
    var name: String,
    @Column(unique = true)
    var email: String,
    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus? = CustomerStatus.ACTIVE,
    @Column
    var password: String,
    @CollectionTable(name = "customer_roles", joinColumns = [JoinColumn(name = "customer_id")])
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @Column
    @Enumerated(EnumType.STRING)
    var roles: Set<Role> = setOf()
) {
    fun toCustomerResponse() =
        CustomerResponse(
            id = this.id,
            name = this.name,
            email = this.email,
            status = this.status!!
        )
}