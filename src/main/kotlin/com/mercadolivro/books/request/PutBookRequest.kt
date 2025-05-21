package com.mercadolivro.books.request

import com.mercadolivro.books.BookModel
import java.math.BigDecimal

data class PutBookRequest(
    var name: String?,
    var price: BigDecimal?
) {
    fun toBookModel(previusValue: BookModel) =
        BookModel(
            id = previusValue.id,
            name = this.name ?: previusValue.name,
            price = this.price ?: previusValue.price,
            status = previusValue.status,
            customer = previusValue.customer
        )
}