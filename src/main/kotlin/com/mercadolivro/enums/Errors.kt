package com.mercadolivro.enums

enum class Errors(val code: String, val message: String) {
    ML_001("ML-001", "Invalid Request"),
    ML_002("ML-002", "Authentication failed"),
    ML_003("ML-003", "Authorization failed"),
    ML_004("ML-004", "Invalid Token"),
    ML_1001("ML-1001", "Book {%s} not found"),
    ML_1002("ML-1002", "Book {%s} not exists"),
    ML_1003("ML-1003", "Cannot update a book with status {%s}"),
    ML_1004("ML-1004", "Books {%s} not found"),
    ML_2001("ML-2001", "Customer {%s} not found"),
    ML_2002("ML-2002", "Customer {%s} not exists"),
    ML_3001("ML-3001", "Cannot purchase books with status different from ACTIVE")
}