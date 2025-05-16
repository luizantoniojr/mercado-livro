package com.mercadolivro.events.listener

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helper.buildPurchaseModel
import com.mercadolivro.service.PurchaseService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class GenerateNfeListenerTest {

    @MockK
    private lateinit var purchaseService: PurchaseService

    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    @Test
    fun `should generate nfe when purchase event is published`() {
        // Arrange
        val fakeNfe = UUID.randomUUID()
        val purchase = buildPurchaseModel(nfe = null)

        val purchaseExpected = purchase.copy(nfe = fakeNfe.toString())
        val purchaseEvent = PurchaseEvent(this, purchase)

        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns fakeNfe
        every { purchaseService.update(purchaseExpected) } returns Unit

        // Act
        generateNfeListener.listen(purchaseEvent)

        // Assert
        verify(exactly = 1) { purchaseService.update(purchaseExpected) }
    }
}