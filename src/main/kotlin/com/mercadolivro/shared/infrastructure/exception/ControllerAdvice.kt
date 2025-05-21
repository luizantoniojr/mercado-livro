package com.mercadolivro.shared.exception

import com.mercadolivro.controller.response.ErrorResponse
import com.mercadolivro.controller.response.FieldErrorResponse
import com.mercadolivro.shared.infrastructure.exception.Errors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    fun handleException(exception: NotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {

        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exception.message,
            exception.errorCode,
            null
        )

        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleException(exception: BadRequestException, request: WebRequest): ResponseEntity<ErrorResponse> {

        val error = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            exception.message,
            exception.errorCode,
            null
        )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(
        exception: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {

        val error = ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            Errors.ML_001.message,
            Errors.ML_001.code,
            exception.bindingResult.fieldErrors.map { FieldErrorResponse(it.defaultMessage ?: "invalid", it.field) }
        )

        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleException(exception: AccessDeniedException, request: WebRequest): ResponseEntity<ErrorResponse> {

        val error = ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            Errors.ML_005.message,
            Errors.ML_005.code,
            null
        )

        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

}