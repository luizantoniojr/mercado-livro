package com.mercadolivro.extension

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel() =
    CustomerModel(
        name = this.name,
        email = this.email,
        status = CustomerStatus.ATIVO
    )

fun PutCustomerRequest.toCustomerModel(previusValue: CustomerModel) =
    CustomerModel(
        id = previusValue.id,
        name = this.name ?: previusValue.name,
        email = this.email ?: previusValue.email,
        status = previusValue.status
    )

fun PostBookRequest.toBookModel(customer: CustomerModel) =
    BookModel(
        name = this.name,
        price = this.price,
        status = BookStatus.ACTIVE,
        customer = customer
    )

fun PutBookRequest.toBookModel(previusValue: BookModel) =
    BookModel(
        id = previusValue.id,
        name = this.name ?: previusValue.name,
        price = this.price ?: previusValue.price,
        status = previusValue.status,
        customer = previusValue.customer
    )

fun CustomerModel.toCustomerResponse() =
    CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status!!
    )

fun BookModel.toBookResponse() =
    BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        status = this.status!!,
        customer = this.customer!!.toCustomerResponse()
    )