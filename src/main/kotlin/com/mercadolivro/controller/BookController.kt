package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.extension.toBookResponse
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("books")
@Tag(name = "Books", description = "Endpoints for Managing Books")
class BookController(private val bookService: BookService, private val customerService: CustomerService) {

    @Operation(summary = "Create book", description = "Create a new book")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Book created successfully"),
            ApiResponse(responseCode = "404", description = "Customer not found"),
            ApiResponse(responseCode = "400", description = "Invalid request data")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postBook(@RequestBody @Valid book: PostBookRequest) {
        val customer = customerService.getById(book.customerId!!)
        if (customer == null) {
            throw NotFoundException(Errors.ML_2001.code, Errors.ML_2001.message.format(book.customerId))
        }

        bookService.create(book.toBookModel(customer))
    }

    @Operation(summary = "List books", description = "Get all books with pagination")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    fun getBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> =
        bookService.getAll(pageable).map { it.toBookResponse() }

    @Operation(summary = "List active books", description = "Get all active books with pagination")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/active")
    fun getActivesBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> =
        bookService.getActives(pageable).map { it.toBookResponse() }

    @Operation(summary = "Find book by ID", description = "Returns a single book")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(responseCode = "404", description = "Book not found")
        ]
    )
    @GetMapping("/{id}")
    fun getBook(@PathVariable id: Int): BookResponse? =
        bookService.getById(id)?.toBookResponse()

    @Operation(summary = "Delete book", description = "Delete an existing book by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            ApiResponse(responseCode = "404", description = "Book not found")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) =
        bookService.delete(id)

    @Operation(summary = "Update book", description = "Update an existing book by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Book updated successfully"),
            ApiResponse(responseCode = "404", description = "Book not found"),
            ApiResponse(responseCode = "400", description = "Invalid request data")
        ]
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putBook(@PathVariable id: Int, @RequestBody book: PutBookRequest) {
        var bookSaved = bookService.getById(id)
        if (bookSaved == null) {
            throw NotFoundException(Errors.ML_1001.code, Errors.ML_1001.message.format(id))
        }

        bookService.update(book.toBookModel(bookSaved))
    }
}