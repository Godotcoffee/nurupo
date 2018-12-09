package com.nurupo.movie.gateway.bean

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateBean {
    @LoadBalanced
    @Bean
    fun restTemplate() = RestTemplate()
}