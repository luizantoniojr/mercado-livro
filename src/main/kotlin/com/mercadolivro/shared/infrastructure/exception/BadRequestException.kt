package com.mercadolivro.shared.exception

class BadRequestException(override val message: String, val errorCode: String) : Exception(message) {
}