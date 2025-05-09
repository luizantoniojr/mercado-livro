package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toCustomerResponse
import com.mercadolivro.service.CustomerService
import jakarta.validation.Valid
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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping("customers")
@Tag(name = "Customers", description = "Endpoints for Managing Customers")
class CustomerController(val customerService: CustomerService) {

    @Operation(summary = "List customers", description = "Get all customers with optional name filter")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    fun getCustomers(@RequestParam name: String?): List<CustomerResponse> =
        customerService.get(name).map { it.toCustomerResponse() }

    @Operation(summary = "Create customer", description = "Create a new customer")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Customer created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid request data")
    ])
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCustomers(@RequestBody @Valid customer: PostCustomerRequest) =
        customerService.create(customer.toCustomerModel())

    @Operation(summary = "Find customer by ID", description = "Returns a single customer")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Success"),
        ApiResponse(responseCode = "404", description = "Customer not found")
    ])
    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: Int): CustomerResponse? =
        customerService.getById(id)?.toCustomerResponse()

    @Operation(summary = "Update customer", description = "Update an existing customer by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Customer updated successfully"),
        ApiResponse(responseCode = "404", description = "Customer not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data")
    ])
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCustomer(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        var customerSaved = customerService.getById(id)
        if (customerSaved == null) {
            throw NotFoundException(Errors.ML_2001.message.format(id), Errors.ML_2001.code)
        }
        customerService.update(customer.toCustomerModel(customerSaved))
    }

    @Operation(summary = "Delete customer", description = "Delete an existing customer by ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        ApiResponse(responseCode = "404", description = "Customer not found")
    ])
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Int) =
        customerService.delete(id)

}