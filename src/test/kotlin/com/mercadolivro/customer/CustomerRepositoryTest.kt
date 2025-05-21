package com.mercadolivro.customer

import com.mercadolivro.helper.buildCustomerModel
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setUp() {
        customerRepository.deleteAll()
    }

    @Test
    fun `should return name containing when name is informed`() {
        // Arrange
        val marcos = customerRepository.save(buildCustomerModel(name = "Marcos"))
        val mateus = customerRepository.save(buildCustomerModel(name = "Mateus"))
        val alex = customerRepository.save(buildCustomerModel(name = "Alex"))
        val expected = listOf(marcos, mateus)

        // Act
        val customers = customerRepository.findByNameContaining("Ma")

        // Assert
        assertEquals(2, customers.size)
        assert(customers.containsAll(expected))
    }

    @Test
    fun `should return empty list when name is not found`() {
        // Arrange
        val marcos = customerRepository.save(buildCustomerModel(name = "Marcos"))
        val mateus = customerRepository.save(buildCustomerModel(name = "Mateus"))
        val alex = customerRepository.save(buildCustomerModel(name = "Alex"))

        // Act
        val customers = customerRepository.findByNameContaining("Lucas")

        // Assert
        assert(customers.isEmpty())
    }

    @Test
    fun `should return true when email exists`() {
        // Arrange
        val email = "customer${UUID.randomUUID()}@email.com"
        val customer = customerRepository.save(buildCustomerModel(email = email))

        // Act
        val exists = customerRepository.existsByEmail(email)

        // Assert
        assert(exists)
    }

    @Test
    fun `should return false when email does not exist`() {
        // Arrange
        val email = "customer${UUID.randomUUID()}@email.com"
        val customer = customerRepository.save(buildCustomerModel(email = email))

        // Act
        val exists = customerRepository.existsByEmail("other${UUID.randomUUID()}@email.com")

        // Assert
        assert(!exists)
    }

    @Test
    fun `should return customer when email exists`() {
        // Arrange
        val email = "customer${UUID.randomUUID()}@email.com"
        val customer = customerRepository.save(buildCustomerModel(email = email))

        // Act
        val foundCustomer = customerRepository.findByEmail(email)

        // Assert
        assertEquals(customer, foundCustomer)
    }

    @Test
    fun `should return null when email does not exist`() {
        // Arrange
        val email = "customer${UUID.randomUUID()}@email.com"
        val customer = customerRepository.save(buildCustomerModel(email = email))

        // Act
        val foundCustomer = customerRepository.findByEmail("other${UUID.randomUUID()}@email.com")

        // Assert
        assert(foundCustomer == null)
    }
}