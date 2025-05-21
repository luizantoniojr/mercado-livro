package com.mercadolivro.books

import com.mercadolivro.customer.CustomerModel
import com.mercadolivro.books.BookStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<BookModel, Int> {
    fun getByStatus(status: BookStatus, pageable: Pageable): Page<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
    fun findAll(pageable: Pageable): Page<BookModel>
}