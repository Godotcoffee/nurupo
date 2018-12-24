package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.config.UserAPIConfig
import com.nurupo.movie.gateway.entity.ResponseJSON
import com.nurupo.movie.gateway.tools.RestJSONHelper
import com.nurupo.movie.gateway.tools.UserHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/{v}/history", produces = ["application/json;charset=utf-8;"])
class HistoryAPIController {
    @LoadBalanced
    @Autowired
    lateinit var restTemplate: RestTemplate

    @PostMapping("/add-item")
    fun addItem(
            @CookieValue(UserAPIConfig.cookieUserIdKey, defaultValue = "-1") userId: String?,
            @CookieValue(UserAPIConfig.cookieUserTokenKey, defaultValue = "") token: String?,
            @PathVariable("v") v: String,
            @RequestBody requestBody: Map<String, Any>): ResponseJSON {
        val uid = requestBody["userId"]?.toString()
        return if (uid == userId && UserHelper.loginValid(UserHelper.userLoginCheck(restTemplate, userId, token, v))) {
            RestJSONHelper.restPost(
                    restTemplate,
                    "http://${ServiceName.nurupoMovieHistory}/$v/history/add-item",
                    requestBody,
                    "URI: /$v/history/add-itemr RETURN NULL"
            )
        } else {
            ResponseJSON(400, null, "Invalid User Id")
        }

    }

    @GetMapping("/item")
    fun item(@PathVariable("v") v: String, httpReq: HttpServletRequest): ResponseJSON {
        val query = httpReq.queryString
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieHistory}/$v/history/item?$query",
                "URI: /$v/history/item RETURN NULL"
        )
    }

    @GetMapping("/page-userId-timestamp")
    fun pageUserIdTimestamp(@PathVariable("v") v: String, httpReq: HttpServletRequest): ResponseJSON {
        val query = httpReq.queryString
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieHistory}/$v/history/page-userId-timestamp?$query",
                "URI: /$v/history/page-userId-timestamp RETURN NULL"
        )
    }

    @GetMapping("/page-userId-rating")
    fun pageUserIdRating(@PathVariable("v") v: String, httpReq: HttpServletRequest): ResponseJSON {
        val query = httpReq.queryString
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieHistory}/$v/history/page-userId-rating?$query",
                "URI: /$v/history/page-userId-rating RETURN NULL"
        )
    }
}