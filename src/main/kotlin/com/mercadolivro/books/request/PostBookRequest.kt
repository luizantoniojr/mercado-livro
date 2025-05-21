package com.mercadolivro.books.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.books.BookModel
import com.mercadolivro.books.BookStatus
import com.mercadolivro.customer.CustomerModel
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
){
    fun toBookModel(customer: CustomerModel) =
        BookModel(
            name = this.name,
            price = this.price!!,
            status = BookStatus.ACTIVE,
            customer = customer
        )
}