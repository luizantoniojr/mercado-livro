package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import com.mercadolivro.helper.buildCustomerModel
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.UserCustomDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeTest
    fun setUp() {
        customerRepository.deleteAll()
    }

    @Test
    fun `should return 200 and all customers`() {
        // Arrange
        val admin = customerRepository.save(buildCustomerModel(roles = setOf(Role.ADMIN)))
        val customer1 = customerRepository.save(buildCustomerModel())
        val customer2 = customerRepository.save(buildCustomerModel())

        // Act & Assert
        mockMvc
            .perform(
                get("/customers")
                    .with(user(UserCustomDetails(admin)))
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[1].id").value(customer1.id))
            .andExpect(jsonPath("$[1].name").value(customer1.name))
            .andExpect(jsonPath("$[1].email").value(customer1.email))
            .andExpect(jsonPath("$[1].status").value(customer1.status.toString()))
            .andExpect(jsonPath("$[2].id").value(customer2.id))
            .andExpect(jsonPath("$[2].name").value(customer2.name))
            .andExpect(jsonPath("$[2].email").value(customer2.email))
            .andExpect(jsonPath("$[2].status").value(customer2.status.toString()))
    }

    @Test
    fun `should return 400 when creating a customer with invalid data`() {
        // Arrange
        val customer = PostCustomerRequest(
            name = "",
            email = "invalid-email",
            password = "123"
        )

        val request = objectMapper.writeValueAsString(customer)

        // Act & Assert
        mockMvc
            .perform(
                post("/customers")
                    .contentType("application/json")
                    .content(request)
            )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
            .andExpect(jsonPath("$.internalCode").value("ML-001"))
    }

    @Test
    fun `should return 200 and customer by name`() {
        // Arrange
        val admin = customerRepository.save(buildCustomerModel(roles = setOf(Role.ADMIN)))
        val customer1 = customerRepository.save(buildCustomerModel(name = "Pedro Silva"))
        customerRepository.save(buildCustomerModel(name = "José Silva"))

        // Act & Assert
        mockMvc
            .perform(
                get("/customers?name=Pedro")
                    .with(user(UserCustomDetails(admin)))
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].status").value(customer1.status.toString()))
    }

    @Test
    fun `should return 201 when creating a customer`() {
        // Arrange
        val email = "${UUID.randomUUID()}@email.com"
        val customer = PostCustomerRequest(
            name = "Pedro Silva",
            email = email,
            password = "123456"
        )

        val request = objectMapper.writeValueAsString(customer)

        // Act & Assert
        mockMvc
            .perform(
                post("/customers")
                    .contentType("application/json")
                    .content(request)
            )
            .andExpect(status().isCreated)

        val customerSaved = customerRepository.findByEmail(email)
        assertEquals(customer.name, customerSaved?.name)
        assertEquals(customer.email, customerSaved?.email)
        assert(customerSaved?.password != null)
        assertEquals(customerSaved?.status, CustomerStatus.ACTIVE)
        assert(customerSaved?.roles?.contains(Role.CUSTOMER) == true)
    }

    @Test
    fun `should return 200 and the customer by id`() {
        // Arrange
        val customer = customerRepository.save(buildCustomerModel())

        // Act & Assert
        mockMvc
            .perform(
                get("/customers/${customer.id}")
                    .with(user(UserCustomDetails(customer)))
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.toString()))
    }

    @Test
    fun `should return 200 and the customer by id when user admin`() {
        // Arrange
        val admin = customerRepository.save(buildCustomerModel(roles = setOf(Role.ADMIN)))
        val customer = customerRepository.save(buildCustomerModel())

        // Act & Assert
        mockMvc
            .perform(
                get("/customers/${customer.id}")
                    .with(user(UserCustomDetails(admin)))
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.toString()))
    }

    @Test
    fun `should return 403 when user tries to get another customer`() {
        // Arrange
        val customer = customerRepository.save(buildCustomerModel())
        val anotherCustomer = customerRepository.save(buildCustomerModel())

        // Act & Assert
        mockMvc
            .perform(
                get("/customers/${customer.id}")
                    .with(user(UserCustomDetails(anotherCustomer)))
            )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `should return 204 when updating a customer`() {
        // Arrange
        val customer = customerRepository.save(buildCustomerModel())
        val updatedCustomer = PostCustomerRequest(
            name = "Updated Name",
            email = "${UUID.randomUUID()}@email.com",
            password = "654321"
        )

        val request = objectMapper.writeValueAsString(updatedCustomer)

        // Act & Assert
        mockMvc
            .perform(
                put("/customers/${customer.id}")
                    .contentType("application/json")
                    .content(request)
                    .with(user(UserCustomDetails(customer)))
            )
            .andExpect(status().isNoContent)

        val customerSaved = customerRepository.findById(customer.id).get()
        assertEquals(updatedCustomer.name, customerSaved.name)
        assertEquals(updatedCustomer.email, customerSaved.email)
    }

    @Test
    fun `should return 404 when updating a customer that does not exist`() {
        // Arrange
        val admin = customerRepository.save(buildCustomerModel(roles = setOf(Role.ADMIN)))
        val updatedCustomer = PostCustomerRequest(
            name = "Updated Name",
            email = "${UUID.randomUUID()}@email.com",
            password = "654321"
        )

        val request = objectMapper.writeValueAsString(updatedCustomer)

        // Act & Assert
        mockMvc
            .perform(
                put("/customers/99999")
                    .contentType("application/json")
                    .content(request)
                    .with(user(UserCustomDetails(admin)))
            )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer {99999} not found"))
            .andExpect(jsonPath("$.internalCode").value("ML-2001"))
    }

    @Test
    fun `should return 204 when deleting a customer`() {
        // Arrange
        val customer = customerRepository.save(buildCustomerModel())

        // Act & Assert
        mockMvc
            .perform(
                delete("/customers/${customer.id}")
                    .with(user(UserCustomDetails(customer)))
            )
            .andExpect(status().isNoContent)

        val customerSaved = customerRepository.findById(customer.id).get()
        assertEquals(CustomerStatus.INACTIVE, customerSaved.status)
    }

    @Test
    fun `should return 404 when deleting a customer that does not exist`() {
        // Arrange
        val admin = customerRepository.save(buildCustomerModel(roles = setOf(Role.ADMIN)))

        // Act & Assert
        mockMvc
            .perform(
                delete("/customers/99999")
                    .with(user(UserCustomDetails(admin)))
            )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer {99999} not found"))
            .andExpect(jsonPath("$.internalCode").value("ML-2001"))
    }
}