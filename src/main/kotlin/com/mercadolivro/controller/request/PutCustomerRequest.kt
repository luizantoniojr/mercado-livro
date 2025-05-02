package com.mercadolivro.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PutCustomerRequest(

    @field:NotEmpty(message = "Name is required")
    var name: String?,

    @field:Email(message = "E-mail is not valid")
    var email: String?
)