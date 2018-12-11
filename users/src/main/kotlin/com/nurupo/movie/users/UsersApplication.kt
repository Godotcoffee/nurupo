package com.nurupo.movie.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import java.util.*
import javax.annotation.PostConstruct

@EnableDiscoveryClient
@SpringBootApplication
class UsersApplication {
    @PostConstruct
    fun setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}

fun main(args: Array<String>) {
    runApplication<UsersApplication>(*args)
}
