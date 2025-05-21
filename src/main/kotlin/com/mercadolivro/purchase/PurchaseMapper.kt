package com.mercadolivro.purchase

import com.mercadolivro.books.BookService
import com.mercadolivro.customer.CustomerService
import com.mercadolivro.purchase.request.PostPurchaseRequest
import com.mercadolivro.shared.exception.NotFoundException
import com.mercadolivro.shared.infrastructure.exception.Errors
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    fun toModel(request: PostPurchaseRequest): PurchaseModel {

        val customer = customerService.getById(request.customerId)
        if (customer == null) {
            throw NotFoundException(Errors.ML_2001.message.format(request.customerId), Errors.ML_2001.code)
        }

        val books = bookService.findAllById(request.bookIds)
        if (books.isEmpty()) {
            throw NotFoundException(Errors.ML_1004.message.format(request.bookIds), Errors.ML_1004.code)
        }

        return PurchaseModel(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}