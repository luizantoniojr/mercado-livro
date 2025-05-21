package com.mercadolivro.shared.validation

import com.mercadolivro.customer.CustomerService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailAvailableValidator(val customerService: CustomerService) : ConstraintValidator<EmailAvailable, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrEmpty()) {
            return false
        }

        return customerService.emailAvailable(value)
    }
}
