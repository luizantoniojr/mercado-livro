package com.mercadolivro.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admins")
@Tag(name = "Admins", description = "Endpoints for Managing Admins")
class AdminController {

    @Operation(summary = "Report", description = "Show a report")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    fun report(): String = "This is a report. Only Admins can see this!"
}