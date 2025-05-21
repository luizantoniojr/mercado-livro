package com.mercadolivro.books

import com.mercadolivro.books.response.BookResponse
import com.mercadolivro.customer.CustomerModel
import com.mercadolivro.shared.exception.BadRequestException
import com.mercadolivro.shared.infrastructure.exception.Errors
import jakarta.persistence.*
import java.math.BigDecimal

@Entity(name = "books")
data class BookModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    @Column
    var name: String,
    @Column
    var price: BigDecimal,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel? = null
) {
    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
            if (field == BookStatus.DELETED) {
                throw BadRequestException(Errors.ML_1003.message.format(BookStatus.DELETED), Errors.ML_1003.code)
            }
            field = value
        }

    constructor(id: Int = 0, name: String, price: BigDecimal, customer: CustomerModel?, status: BookStatus?)
            : this(id, name, price, customer) {
        this.status = status
    }

    fun toBookResponse() =
        BookResponse(
            id = this.id,
            name = this.name,
            price = this.price,
            status = this.status!!,
            customer = this.customer!!.toCustomerResponse()
        )
}