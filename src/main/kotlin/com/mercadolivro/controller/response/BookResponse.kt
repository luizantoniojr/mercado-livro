package com.mercadolivro.controller.response

import com.mercadolivro.enums.BookStatus
import java.math.BigDecimal

data class BookResponse (
    var id: Int,
    var name: String,
    var price: BigDecimal,
    var status: BookStatus,
    var customer: CustomerResponse
)