package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.extension.toBookResponse
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("books")
class BookController(val bookService: BookService, val customerService: CustomerService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postBook(@RequestBody book: PostBookRequest) {
        val customer = customerService.getById(book.customerId)
        if (customer == null) {
            throw Exception("Id ${book.customerId} invalid")
        }

        bookService.create(book.toBookModel(customer))
    }

    @GetMapping
    fun getBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> =
        bookService.getAll(pageable).map { it.toBookResponse() }

    @GetMapping("/active")
    fun getActivesBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> =
        bookService.getActives(pageable).map { it.toBookResponse() }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id: Int): BookResponse? =
        bookService.getById(id)?.toBookResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) =
        bookService.delete(id)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putBook(@PathVariable id: Int, @RequestBody book: PutBookRequest) {
        var bookSaved = bookService.getById(id)
        if (bookSaved == null) {
            throw Exception("Id $id not found")
        }

        bookService.update(book.toBookModel(bookSaved))
    }
}