package com.mercadolivro.customer.response

import com.mercadolivro.customer.CustomerStatus

data class CustomerResponse (
    var id: Int,
    var name: String,
    var email: String,
    var status: CustomerStatus,
    var roles: Set<String> = setOf()
)