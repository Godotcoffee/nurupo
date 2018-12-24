package com.nurupo.movie.gateway.tools

import com.nurupo.movie.gateway.entity.ResponseJSON
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

object RestJSONHelper {
    fun restPost(restTemplate: RestTemplate, url: String, request: Any, nurupo: String): ResponseJSON {
        return try {
            restTemplate.postForObject(
                    url,
                    HttpEntity(request, HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON_UTF8 }),
                    ResponseJSON::class.java
            ) ?: ResponseJSON(-254, msg = nurupo)
        } catch (e: Exception) {
            ResponseJSON(-255, e.stackTrace, "Exception")
        }
    }

    fun restGet(restTemplate: RestTemplate, url: String, nurupo: String): ResponseJSON {
        return try {
            restTemplate.getForObject(
                    url,
                    ResponseJSON::class.java
            ) ?: ResponseJSON(-254, msg = nurupo)
        } catch (e: Exception) {
            ResponseJSON(-255, e.stackTrace, "Exception")
        }
    }
}