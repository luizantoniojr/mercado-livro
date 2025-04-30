package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class BookService(val bookRepository: BookRepository) {

    fun create(book: BookModel) =
        bookRepository.save(book)

    fun getAll(pageable: Pageable): Page<BookModel> =
        bookRepository.findAll(pageable)

    fun getActives(pageable: Pageable): Page<BookModel> =
        bookRepository.getByStatus(BookStatus.ACTIVE, pageable)

    fun getById(id: Int): BookModel? =
        bookRepository.findById(id).getOrNull()

    fun delete(id: Int) {
        var book = getById(id)
        if (book == null) {
            throw Exception("Id $id not found")
        }

        book.status = BookStatus.DELETED
        bookRepository.save(book)
    }

    fun update(book: BookModel) {
        if (!bookRepository.existsById(book.id)) {
            throw Exception("Id ${book.id} not found")
        }
        bookRepository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCustomer(customer)
        for (book in books) {
            book.status = BookStatus.DELETED
        }

        bookRepository.saveAll(books)
    }
}