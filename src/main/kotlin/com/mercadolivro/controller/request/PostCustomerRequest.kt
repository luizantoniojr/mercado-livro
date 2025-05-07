package com.mercadolivro.controller.request

import com.mercadolivro.validation.EmailAvailable
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
)