package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
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
            throw NotFoundException(Errors.ML_1001.message.format(id), Errors.ML_1001.code)
        }

        book.status = BookStatus.DELETED
        bookRepository.save(book)
    }

    fun update(book: BookModel) {
        if (!bookRepository.existsById(book.id)) {
            throw NotFoundException(Errors.ML_1002.message.format(book.id), Errors.ML_1002.code)
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