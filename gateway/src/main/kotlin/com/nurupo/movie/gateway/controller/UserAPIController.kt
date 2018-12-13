package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.config.UserAPIConfig
import com.nurupo.movie.gateway.entity.ResponseJSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
@RequestMapping("/{v}/user")
class UserAPIController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @PostMapping("/register", produces = ["application/json;charset=utf-8;"])
    fun userRegister(@RequestBody request: String, @PathVariable("v") v: String): ResponseJSON {
        return restPostHelper(
                "http://${ServiceName.nurupoMovieUsers}/$v/user/register",
                request,
                "URI: /$v/user/register RETURN NULL")
    }

    @PostMapping("/login", produces = ["application/json;charset=utf-8;"])
    fun userLogin(@RequestBody request: String, @PathVariable("v") v: String, response: HttpServletResponse): ResponseJSON {
        // Login
        val resp = restPostHelper(
                "http://${ServiceName.nurupoMovieUsers}/$v/user/login",
                request,
                "URI: /$v/user/login RETURN NULL"
        )
        try {
            if (resp.status == 0) {
                return if (resp.obj != null && resp.obj is Map<*, *>) {
                    val data = resp.obj as Map<*, *>
                    val userId = data["userId"]
                    val token = data["token"]
                    if (userId != null && token != null) {
                        // Save cookies
                        response.addCookie(Cookie(UserAPIConfig.cookieUserIdKey, userId.toString()).apply { maxAge = UserAPIConfig.cookieMaxAge })
                        response.addCookie(Cookie(UserAPIConfig.cookieUserTokenKey, token.toString()).apply { maxAge = UserAPIConfig.cookieMaxAge })
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
        return ResponseJSON(0, loginValid(userLoginCheckHelper(userId, token, v)))
    }

    @GetMapping("/logout")
    fun logout(
            @CookieValue(UserAPIConfig.cookieUserIdKey, defaultValue = "-1") userId: String?,
            @CookieValue(UserAPIConfig.cookieUserTokenKey, defaultValue = "") token: String?,
            @PathVariable("v") v: String): ResponseJSON {
        val resp = userLoginCheckHelper(userId, token, v)

        if (loginValid(resp)) {
            return restGetHelper(
                "http://${ServiceName.nurupoMovieUsers}/$v/user/logout/$userId",
                "URI: /$v/user/logout RETURN NULL"
        )
        }
        return ResponseJSON(0, msg = "No login")
    }

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable("id") id: Int, @PathVariable("v") v: String): ResponseJSON {
        return restGetHelper(
                "http://${ServiceName.nurupoMovieUsers}/$v/user/id/$id",
                "URI: /$v/user/logout RETURN NULL"
        )
    }

    private fun restPostHelper(url: String, request: Any, nurupo: String): ResponseJSON {
        return try {
            restTemplate.postForObject(
                    url,
                    HttpEntity(request, HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON_UTF8 }),
                    ResponseJSON::class.java
            ) ?: ResponseJSON(-254, msg = nurupo)
        } catch (e: Exception) {
            ResponseJSON(-255, e.message, "Exception")
        }
    }

    private fun restGetHelper(url: String, nurupo: String): ResponseJSON {
        return try {
            restTemplate.getForObject(
                    url,
                    ResponseJSON::class.java
            ) ?: ResponseJSON(-254, msg = nurupo)
        } catch (e: Exception) {
            ResponseJSON(-255, e.message, "Exception")
        }
    }

    private fun userLoginCheckHelper(id: String?, token: String?, v: String): ResponseJSON {
        return restGetHelper(
                "http://${ServiceName.nurupoMovieUsers}/$v/user/token/$id/$token",
                "URI: /$v/user/token RETURN NULL"
        )
    }

    private fun loginValid(resp: ResponseJSON?): Boolean {
        return resp != null && resp.status == 0 && (resp.obj == true || "true".equals(resp.obj?.toString() ?: "false", true))
    }
}