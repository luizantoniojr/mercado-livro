package com.mercadolivro.purchase

import com.mercadolivro.books.BookStatus
import com.mercadolivro.shared.exception.NotFoundException
import com.mercadolivro.shared.infrastructure.exception.Errors
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun create(purchaseModel: PurchaseModel) {
        if (purchaseModel.books.any { it.status != BookStatus.ACTIVE }) {
            throw NotFoundException(Errors.ML_3001.message, Errors.ML_3001.code)
        }
        purchaseRepository.save(purchaseModel)
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }
}