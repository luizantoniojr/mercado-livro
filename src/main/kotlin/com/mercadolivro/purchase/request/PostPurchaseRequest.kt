package com.mercadolivro.purchase.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class PostPurchaseRequest(
    @JsonAlias("customer_id")
    @field:NotNull(message = "Customer is required")
    @field:Positive(message = "Customer must be greater than zero")
    val customerId: Int,

    @JsonAlias("book_ids")
    @field:NotNull(message = "Books is required")
    @field: NotEmpty(message = "Books is required")
    val bookIds: Set<Int>
)