package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.entity.ResponseJSON
import com.nurupo.movie.gateway.tools.RestJSONHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest

@CrossOrigin
@RestController
@RequestMapping("/{v}/movie", produces = ["application/json;charset=utf-8;"])
class MovieAPIController {
    @LoadBalanced
    @Autowired
    lateinit var restTemplate: RestTemplate

    @GetMapping("/all")
    fun getAllMovie(httpReq: HttpServletRequest, @PathVariable("v") v: String): ResponseJSON {
        val query = httpReq.queryString
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/all?$query",
                "URI: /$v/movie/all?$query RETURN NULL"
        )
    }

    @GetMapping("/type")
    fun getAllType(@PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/type",
                "URI: /$v/movie/type RETURN NULL"
        )
    }

    @GetMapping("/init")
    fun initMovie(@PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/init",
                "URI: /$v/movie/init RETURN NULL"
        )
    }
}
