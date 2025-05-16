package com.mercadolivro.helper

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.PurchaseModel
import java.math.BigDecimal
import java.util.Random
import java.util.UUID

fun buildPurchaseModel(
    id: Int = Random().nextInt(),
    customer: CustomerModel = buildCustomerModel(),
    books: List<BookModel> = listOf(buildBookModel()),
    nfe: String? = UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.valueOf(Random().nextDouble() * 100)
) = PurchaseModel(
    id = id,
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)

fun buildCustomerModel(
    id: Int = Random().nextInt(),
    name: String = "Customer",
    email: String = "${UUID.randomUUID()}@email.com",
    password: String = "123456",
    status: CustomerStatus = CustomerStatus.ACTIVE,
    roles: Set<Role> = setOf(Role.CUSTOMER)
) = CustomerModel(
    id = id,
    name = name,
    email = email,
    password = password,
    status = status,
    roles = roles
)

fun buildBookModel(
    id: Int = Random().nextInt(),
    name: String = "Book",
    price: BigDecimal = BigDecimal.valueOf(Random().nextDouble() * 100),
    status: BookStatus = BookStatus.ACTIVE,
    customer: CustomerModel? = null
) = BookModel(
    id = id,
    name = name,
    price = price,
    status = status,
    customer = customer
)