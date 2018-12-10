package com.nurupo.movie.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class UsersApplication

fun main(args: Array<String>) {
    runApplication<UsersApplication>(*args)
}
