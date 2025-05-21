package com.mercadolivro.books.response

import com.mercadolivro.customer.response.CustomerResponse
import com.mercadolivro.books.BookStatus
import java.math.BigDecimal

data class BookResponse (
    var id: Int,
    var name: String,
    var price: BigDecimal,
    var status: BookStatus,
    var customer: CustomerResponse
)