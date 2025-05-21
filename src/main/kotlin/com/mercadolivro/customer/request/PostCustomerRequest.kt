package com.mercadolivro.customer.request

import com.mercadolivro.customer.CustomerModel
import com.mercadolivro.customer.CustomerStatus
import com.mercadolivro.shared.validation.EmailAvailable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PostCustomerRequest(

    @field:NotEmpty(message = "Name is required")
    var name: String,

    @field:Email(message = "E-mail is not valid")
    @EmailAvailable
    var email: String,

    @field:NotEmpty(message = "Password is required")
    var password: String
) {
    fun toCustomerModel() =
        CustomerModel(
            name = this.name,
            email = this.email,
            status = CustomerStatus.ACTIVE,
            password = this.password
        )
}