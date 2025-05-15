package com.mercadolivro.config

import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Role
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JWTUtil,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint
) {

    private val PUBLIC_MATCHERS = arrayOf(
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-resources/**"
    )

    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/customers",
        "/login"
    )

    private val ADMIN_MATCHERS = arrayOf(
        "/admins/**"
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        val authenticationManager = authenticationConfiguration.authenticationManager

        val formLogin = http
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers(*PUBLIC_MATCHERS).permitAll()
                    .requestMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
                    .requestMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
                    .anyRequest().authenticated()
            }
            .cors { }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .addFilter(AuthenticationFilter(authenticationManager, jwtUtil))
            .addFilter(AuthorizationFilter(authenticationManager, jwtUtil, userDetailsService(customerRepository)))
            .authenticationManager(authenticationManager)
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint(authenticationEntryPoint)
            }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("*")
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        config.allowCredentials = false

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(customerRepository: CustomerRepository): UserDetailsService {
        return UserDetailsService { email ->
            val customer = customerRepository.findByEmail(email)
                ?: throw NotFoundException(Errors.ML_2001.message, Errors.ML_2001.code)

            UserCustomDetails(customer)
        }
    }

    @Bean
    fun authenticationManager(
        http: HttpSecurity,
        passwordEncoder: BCryptPasswordEncoder,
        userDetailsService: UserDetailsService
    ): AuthenticationManager {
        val builder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        builder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)

        return builder.build()
    }
}
