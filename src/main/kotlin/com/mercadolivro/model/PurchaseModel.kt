package com.mercadolivro.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "purchases")
data class PurchaseModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel,

    @ManyToMany
    @JoinTable(
        name = "purchase_books",
        joinColumns = [JoinColumn(name = "purchase_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")]
    )
    var books: List<BookModel>,

    @Column
    val nfe: String? = null,

    @Column
    val price: BigDecimal,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)