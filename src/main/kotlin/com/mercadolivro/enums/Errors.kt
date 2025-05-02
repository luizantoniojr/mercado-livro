package com.mercadolivro.enums

enum class Errors(val code: String, val message: String) {
    ML_001("ML-001", "Invalid Request"),
    ML_1001("ML-1001", "Book {%s} not found"),
    ML_1002("ML-1002", "Book {%s} not exists"),
    ML_1003("ML-1003", "Cannot delete a book with status {%s}"),
    ML_2001("ML-2001", "Customer {%s} not found"),
    ML_2002("ML-2002", "Customer {%s} not exists")
}