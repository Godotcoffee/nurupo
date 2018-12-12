package com.nurupo.movie.users.bean

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class ValidatorBean {
    @Bean
    fun validator() = LocalValidatorFactoryBean()
}