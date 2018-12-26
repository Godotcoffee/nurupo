package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.config.UserAPIConfig
import com.nurupo.movie.gateway.entity.ResponseJSON
import com.nurupo.movie.gateway.tools.RestJSONHelper
import com.nurupo.movie.gateway.tools.UserHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@CrossOrigin("*", allowCredentials = "true")
@RestController
@RequestMapping("/{v}/user", produces = ["application/json;charset=utf-8;"])
class UserAPIController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @PostMapping("/register")
    fun userRegister(@RequestBody request: String, @PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restPost(
                restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/register",
                request)
    }

    @PostMapping("/login")
    fun userLogin(@RequestBody request: String, @PathVariable("v") v: String, response: HttpServletResponse): ResponseJSON {
        // Login
        val resp = RestJSONHelper.restPost(
                restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/login",
                request
        )
        try {
            if (resp.status == 0) {
                return if (resp.obj != null && resp.obj is Map<*, *>) {
                    val data = resp.obj as Map<*, *>
                    val userId = data["userId"]
                    val token = data["token"]
                    if (userId != null && token != null) {
                        // Save cookies
                        response.addCookie(Cookie(UserAPIConfig.cookieUserIdKey, userId.toString()).apply {
                            maxAge = UserAPIConfig.cookieMaxAge; path = "/" })
                        response.addCookie(Cookie(UserAPIConfig.cookieUserTokenKey, token.toString()).apply {
                            maxAge = UserAPIConfig.cookieMaxAge; path = "/" })
                        ResponseJSON(0, mapOf("userId" to userId))
                    } else {
                        ResponseJSON(-2, msg = "WTF: Id or token is bad")
                    }
                } else {
                    ResponseJSON(-1, msg = "WTF: Object is bad")
                }
            } else {
                return resp;
            }
        } catch (e: Exception) {
            return ResponseJSON(-255, e.message, "Exception")
        }
    }

    @GetMapping("/loginCheck")
    fun userLoginCheck(
            @CookieValue(UserAPIConfig.cookieUserIdKey, defaultValue = "-1") userId: String?,
            @CookieValue(UserAPIConfig.cookieUserTokenKey, defaultValue = "") token: String?,
            @PathVariable("v") v: String): ResponseJSON {
        return ResponseJSON(0, mapOf(
                "login" to if (UserHelper.loginValid(UserHelper.userLoginCheck(restTemplate, userId, token, v))) 1 else 0,
                "userId" to userId?.toIntOrNull()))
    }

    @GetMapping("/logout")
    fun logout(
            @CookieValue(UserAPIConfig.cookieUserIdKey, defaultValue = "-1") userId: String?,
            @CookieValue(UserAPIConfig.cookieUserTokenKey, defaultValue = "") token: String?,
            @PathVariable("v") v: String): ResponseJSON {
        val resp = UserHelper.userLoginCheck(restTemplate, userId, token, v)

        if (UserHelper.loginValid(resp)) {
            return RestJSONHelper.restGet(
                    restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/logout/$userId"
            )
        }
        return ResponseJSON(0, msg = "No login")
    }

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable("id") id: Int, @PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/id/$id"
        )
    }
}
