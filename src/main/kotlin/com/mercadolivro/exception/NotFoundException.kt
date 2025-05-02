package com.mercadolivro.exception

class NotFoundException(override val message: String = "Resource Not Found", val errorCode: String) :
    Exception(message) {
}