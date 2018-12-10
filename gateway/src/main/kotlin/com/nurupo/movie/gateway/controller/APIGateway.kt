package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.entity.ResponseJSON
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class APIGateway {
    @Autowired
    lateinit var restTemplate: RestTemplate

    @PostMapping("/test/json", produces = ["application/json;charset=utf-8;"])
    fun testForJSON(@RequestBody jsonStr: String): ResponseJSON {
        println(jsonStr)
        val result = restTemplate.postForObject(
                "http://${ServiceName.NURUPO_MOVIE_SPARK}/sum",
                HttpEntity(jsonStr, HttpHeaders().let { it.contentType = MediaType.APPLICATION_JSON_UTF8; it}),
                ResponseJSON::class.java
        )

        return result ?: ResponseJSON(-255, msg="NURUPO~ GA!")
    }
}