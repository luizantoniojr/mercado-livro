package com.mercadolivro.customer

import com.mercadolivro.books.BookService
import com.mercadolivro.helper.buildCustomerModel
import com.mercadolivro.shared.exception.NotFoundException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.Optional
import java.util.Random
import java.util.UUID
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        //Arrange
        val fakeCustomers = listOf(buildCustomerModel(), buildCustomerModel())
        every { customerRepository.findAll() } returns fakeCustomers

        //Act
        val customers = customerService.get(null)

        //Assert
        Assertions.assertEquals(fakeCustomers, customers)
        verify(exactly = 1) { customerRepository.findAll() }
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }
    }

    @Test
    fun `should return customers by name when name is informed`() {
        //Arrange
        val name = "Customer ${UUID.randomUUID()}"
        val fakeCustomers = listOf(buildCustomerModel(), buildCustomerModel())
        every { customerRepository.findByNameContaining(name) } returns fakeCustomers

        //Act
        val customers = customerService.get(name)

        //Assert
        Assertions.assertEquals(fakeCustomers, customers)
        verify(exactly = 1) { customerRepository.findByNameContaining(name) }
        verify(exactly = 0) { customerRepository.findAll() }
    }

    @Test
    fun `should create customer and encrypt password`() {
        //Arrange
        val fakeCustomer = buildCustomerModel()
        val encryptedPassword = "encryptedPassword ${UUID.randomUUID()}"
        val fakeCustomerWithEncryptedPassword = fakeCustomer.copy(password = encryptedPassword)
        every { customerRepository.save(any()) } returns fakeCustomerWithEncryptedPassword
        every { bCrypt.encode(fakeCustomer.password) } returns encryptedPassword

        //Act
        customerService.create(fakeCustomer)

        //Assert
        verify(exactly = 1) { customerRepository.save(fakeCustomerWithEncryptedPassword) }
        verify(exactly = 1) { bCrypt.encode(fakeCustomer.password) }
    }

    @Test
    fun `should return customer by id`() {
        //Arrange
        val id = Random().nextInt()
        val fakeCustomer = buildCustomerModel(id = id)
        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        //Act
        val customers = customerService.getById(id)

        //Assert
        Assertions.assertEquals(fakeCustomer, customers)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should return null when customer not found`() {
        //Arrange
        val id = Random().nextInt()
        every { customerRepository.findById(id) } returns Optional.empty()

        //Act
        val customers = customerService.getById(id)

        //Assert
        Assertions.assertEquals(null, customers)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        //Arrange
        val fakeCustomer = buildCustomerModel()
        every { customerRepository.existsById(fakeCustomer.id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        //Act
        customerService.update(fakeCustomer)

        //Assert
        verify(exactly = 1) { customerRepository.existsById(fakeCustomer.id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should throw NotFoundException when customer not found for update`() {
        //Arrange
        val fakeCustomer = buildCustomerModel()
        every { customerRepository.existsById(fakeCustomer.id) } returns false

        //Act
        val error = assertThrows<NotFoundException> { customerService.update(fakeCustomer) }

        //Assert
        Assertions.assertEquals("Customer {${fakeCustomer.id}} not exists", error.message)
        verify(exactly = 1) { customerRepository.existsById(fakeCustomer.id) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should delete customer and set status to INACTIVE`() {
        //Arrange
        val fakeCustomer = buildCustomerModel()
        every { customerService.getById(fakeCustomer.id) } returns fakeCustomer
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer
        every { bookService.deleteByCustomer(fakeCustomer) } returns Unit

        //Act
        customerService.delete(fakeCustomer.id)

        //Assert
        Assertions.assertEquals(CustomerStatus.INACTIVE, fakeCustomer.status)
        verify(exactly = 1) { customerService.getById(fakeCustomer.id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
    }

    @Test
    fun `should throw NotFoundException when customer not found for delete`() {
        //Arrange
        val id = Random().nextInt()
        every { customerService.getById(id) } returns null

        //Act
        val error = assertThrows<NotFoundException> { customerService.delete(id) }

        //Assert
        Assertions.assertEquals("Customer {$id} not found", error.message)
        verify(exactly = 1) { customerService.getById(id) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should returns true when email available`() {
        //Arrange
        val email = "${UUID.randomUUID()}@email.com"
        every { customerRepository.existsByEmail(email) } returns false

        //Act
        val result = customerService.emailAvailable(email)

        //Assert
        Assertions.assertEquals(true, result)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    @Test
    fun `should returns false when email not available`() {
        //Arrange
        val email = "${UUID.randomUUID()}@email.com"
        every { customerRepository.existsByEmail(email) } returns true

        //Act
        val result = customerService.emailAvailable(email)

        //Assert
        Assertions.assertEquals(false, result)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }
}