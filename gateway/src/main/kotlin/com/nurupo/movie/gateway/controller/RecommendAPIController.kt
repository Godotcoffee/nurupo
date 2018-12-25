package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.entity.ResponseJSON
import com.nurupo.movie.gateway.tools.RestJSONHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/{v}/recommend/", produces = ["application/json;charset=utf-8;"])
class RecommendAPIController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @GetMapping("/task")
    fun task(@PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieRecommend}/$v/recommend/task"
        )
    }

    @GetMapping("/id/{userId}")
    fun id(@PathVariable("v") v: String, @PathVariable("userId") userId: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieRecommend}/$v/recommend/id/$userId"
        )
    }
}