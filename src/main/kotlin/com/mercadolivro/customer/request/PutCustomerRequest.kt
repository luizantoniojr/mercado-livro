package com.mercadolivro.customer.request

import com.mercadolivro.customer.CustomerModel
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PutCustomerRequest(

    @field:NotEmpty(message = "Name is required")
    var name: String?,

    @field:Email(message = "E-mail is not valid")
    var email: String?
) {
    fun toCustomerModel(previusValue: CustomerModel) =
        CustomerModel(
            id = previusValue.id,
            name = this.name ?: previusValue.name,
            email = this.email ?: previusValue.email,
            status = previusValue.status,
            password = previusValue.password
        )
}