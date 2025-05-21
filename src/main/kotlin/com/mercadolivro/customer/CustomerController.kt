package com.mercadolivro.customer

import com.mercadolivro.customer.request.PostCustomerRequest
import com.mercadolivro.customer.request.PutCustomerRequest
import com.mercadolivro.customer.response.CustomerResponse
import com.mercadolivro.shared.exception.NotFoundException
import com.mercadolivro.shared.infrastructure.exception.Errors
import com.mercadolivro.shared.security.OnlyAdminCanAccess
import com.mercadolivro.shared.security.UserCanOnlyAccessTheirOwnResource
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customers")
@Tag(name = "Customers", description = "Endpoints for Managing Customers")
class CustomerController(private val customerService: CustomerService) {

    @Operation(summary = "List customers", description = "Get all customers with optional name filter")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    @OnlyAdminCanAccess
    fun getCustomers(@RequestParam name: String?): List<CustomerResponse> =
        customerService.get(name).map { it.toCustomerResponse() }

    @Operation(summary = "Create customer", description = "Create a new customer")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Customer created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request data")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCustomers(@RequestBody @Valid customer: PostCustomerRequest) =
        customerService.create(customer.toCustomerModel())

    @Operation(summary = "Find customer by ID", description = "Returns a single customer")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(responseCode = "404", description = "Customer not found")
        ]
    )
    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    fun getCustomer(@PathVariable id: Int): CustomerResponse? =
        customerService.getById(id)?.toCustomerResponse()

    @Operation(summary = "Update customer", description = "Update an existing customer by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Customer updated successfully"),
            ApiResponse(responseCode = "404", description = "Customer not found"),
            ApiResponse(responseCode = "400", description = "Invalid request data")
        ]
    )
    @PutMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCustomer(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        var customerSaved = customerService.getById(id)
        if (customerSaved == null) {
            throw NotFoundException(Errors.ML_2001.message.format(id), Errors.ML_2001.code)
        }
        customerService.update(customer.toCustomerModel(customerSaved))
    }

    @Operation(summary = "Delete customer", description = "Delete an existing customer by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            ApiResponse(responseCode = "404", description = "Customer not found")
        ]
    )
    @DeleteMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Int) =
        customerService.delete(id)

}