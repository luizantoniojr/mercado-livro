package com.mercadolivro.books

import com.mercadolivro.customer.CustomerModel
import com.mercadolivro.shared.exception.NotFoundException
import com.mercadolivro.shared.infrastructure.exception.Errors
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class BookService(private val bookRepository: BookRepository) {

    fun create(book: BookModel) =
        bookRepository.save(book)

    fun getAll(pageable: Pageable): Page<BookModel> =
        bookRepository.findAll(pageable)

    fun getActives(pageable: Pageable): Page<BookModel> =
        bookRepository.getByStatus(BookStatus.ACTIVE, pageable)

    fun getById(id: Int): BookModel? =
        bookRepository.findById(id).getOrNull()

    fun delete(id: Int) {
        val book = getById(id)
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

    fun findAllById(bookIds: Set<Int>): List<BookModel> =
        bookRepository.findAllById(bookIds).toList()

    fun purchase(books: MutableList<BookModel>) {
        books.map { it.status = BookStatus.SOLD }
        bookRepository.saveAll(books)
    }
}