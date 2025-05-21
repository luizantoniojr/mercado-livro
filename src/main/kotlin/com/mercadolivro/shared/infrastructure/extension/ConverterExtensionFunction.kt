package com.mercadolivro.shared.extension

import com.mercadolivro.controller.response.PageResponse
import org.springframework.data.domain.Page

fun <T> Page<T>.toPageResponse(): PageResponse<T> =
    PageResponse(
        items = this.content,
        currentPage = this.number,
        totalItems = this.totalElements,
        totalPages = this.totalPages
    )
