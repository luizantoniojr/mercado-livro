package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toCustomerResponse
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("customers")
class CustomerController(val customerService: CustomerService) {

    @GetMapping
    fun getCustomers(@RequestParam name: String?): List<CustomerResponse> =
         customerService.get(name).map { it.toCustomerResponse() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCustomers(@RequestBody customer: PostCustomerRequest) =
        customerService.create(customer.toCustomerModel())

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: Int): CustomerResponse? =
         customerService.getById(id)?.toCustomerResponse()

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCustomer(@PathVariable id: Int, @RequestBody customer: PutCustomerRequest) {
        var customerSaved = customerService.getById(id)
        if (customerSaved == null) {
            throw Exception("Id $id not found")
        }
        customerService.update(customer.toCustomerModel(customerSaved))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Int) =
        customerService.delete(id)

}