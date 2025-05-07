package com.mercadolivro.model

import com.mercadolivro.enums.CustomerStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

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
    var status: CustomerStatus? = CustomerStatus.ATIVO,
    @Column
    var password: String
)