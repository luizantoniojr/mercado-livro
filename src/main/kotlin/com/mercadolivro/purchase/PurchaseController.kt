package com.mercadolivro.purchase

import com.mercadolivro.purchase.PurchaseMapper
import com.mercadolivro.purchase.request.PostPurchaseRequest
import com.mercadolivro.purchase.PurchaseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("purchases")
@Tag(name = "Purchases", description = "Endpoints for Managing Book Purchases")
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper
) {

    @Operation(summary = "Create purchase", description = "Create a new book purchase")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Purchase created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request data"),
            ApiResponse(responseCode = "404", description = "Book or customer not found")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun purchase(@RequestBody request: PostPurchaseRequest) {
        val model = purchaseMapper.toModel(request)
        purchaseService.create(model)
    }
}