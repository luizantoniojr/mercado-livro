package com.mercadolivro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@EnableAsync
@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
class MercadoLivroApplication

fun main(args: Array<String>) {
    runApplication<MercadoLivroApplication>(*args)
}
