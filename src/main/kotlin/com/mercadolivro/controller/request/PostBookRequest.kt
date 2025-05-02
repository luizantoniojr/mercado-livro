package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PostBookRequest(

    @field:NotEmpty(message = "Name is required")
    var name: String,

    @field:NotNull(message = "Price is required")
    var price: BigDecimal?,

    @JsonAlias("customer_id")
    @field:NotNull(message = "Customer is required")
    var customerId: Int?
)