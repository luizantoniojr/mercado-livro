package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService
) {

    fun get(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(name)
        }

        return customerRepository.findAll().toList()
    }

    fun create(customer: CustomerModel) =
        customerRepository.save(customer)


    fun getById(id: Int): CustomerModel? =
        customerRepository.findById(id).getOrNull()


    fun update(customer: CustomerModel) {
        if (!customerRepository.existsById(customer.id)) {
            throw Exception("Id ${customer.id} not found")
        }
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        var customer = getById(id)
        if (customer == null) {
            throw Exception("Id $id not found")
        }

        customer.status = CustomerStatus.INATIVO
        customerRepository.save(customer)
        bookService.deleteByCustomer(customer)
    }
}