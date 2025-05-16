package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.helper.buildBookModel
import com.mercadolivro.helper.buildPurchaseModel
import com.mercadolivro.repository.PurchaseRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {
    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `should create a purchase and publish an event`() {
        // Arrange
        val purchaseModel = buildPurchaseModel()
        every { purchaseRepository.save(purchaseModel) } returns purchaseModel
        every { applicationEventPublisher.publishEvent(any()) } returns Unit

        // Act
        purchaseService.create(purchaseModel)

        // Assert
        verify(exactly = 1) { purchaseRepository.save(purchaseModel) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(purchaseModel, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun `should throw an exception when trying to create a purchase with inactive books`() {
        // Arrange
        val purchaseModel = buildPurchaseModel(books = listOf(buildBookModel(status = BookStatus.DELETED)))

        // Act & Assert
        val exception = assertThrows<NotFoundException> {
            purchaseService.create(purchaseModel)
        }

        assertEquals("Cannot purchase books with status different from ACTIVE", exception.message)
    }

    @Test
    fun `should update a purchase`() {
        // Arrange
        val purchaseModel = buildPurchaseModel()
        every { purchaseRepository.save(purchaseModel) } returns purchaseModel

        // Act
        purchaseService.update(purchaseModel)

        // Assert
        verify(exactly = 1) { purchaseRepository.save(purchaseModel) }
    }
}