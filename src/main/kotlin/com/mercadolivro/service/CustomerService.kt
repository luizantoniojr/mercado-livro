package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
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
            throw NotFoundException(Errors.ML_2002.message.format(customer.id), Errors.ML_2002.code)
        }
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        var customer = getById(id)
        if (customer == null) {
            throw NotFoundException(Errors.ML_2001.message.format(id), Errors.ML_2001.code)
        }

        customer.status = CustomerStatus.INATIVO
        customerRepository.save(customer)
        bookService.deleteByCustomer(customer)
    }

    fun emailAvailable(value: String): Boolean =
        !customerRepository.existsByEmail(value);
}