package com.mercadolivro.extension

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel() = CustomerModel(name = name, email = email)

fun PutCustomerRequest.toCustomerModel(id: Int) = CustomerModel(id, name, email)