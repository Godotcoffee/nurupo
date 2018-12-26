package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.entity.ResponseJSON
import com.nurupo.movie.gateway.tools.RestJSONHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest

@CrossOrigin("*", allowCredentials = "true")
@RestController
@RequestMapping("/{v}/movie", produces = ["application/json;charset=utf-8;"])
class MovieAPIController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @GetMapping("/id/{movieId}")
    fun id(@PathVariable("movieId") movieId: String, @PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/id/$movieId"
        )
    }

    @GetMapping("/ids/{movies}")
    fun ids(@PathVariable("movies") movies: String, @PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/ids/$movies"
        )
    }

    @GetMapping("/all")
    fun getAllMovie(httpReq: HttpServletRequest, @PathVariable("v") v: String): ResponseJSON {
        val query = httpReq.queryString
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/all?$query"
        )
    }

    @GetMapping("/type")
    fun getAllType(@PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/type"
        )
    }

    @GetMapping("/search")
    fun movieSearch(httpReq: HttpServletRequest, @PathVariable("v") v: String): ResponseJSON {
        val query = httpReq.queryString
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieMovie}/$v/movie/search?$query"
        )
    }

}
