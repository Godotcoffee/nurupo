package com.nurupo.movie.gateway.controller

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.config.UserAPIConfig
import com.nurupo.movie.gateway.entity.ResponseJSON
import com.nurupo.movie.gateway.tools.RestJSONHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@CrossOrigin
@RestController
@RequestMapping("/{v}/user", produces = ["application/json;charset=utf-8;"])
class UserAPIController {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @PostMapping("/register")
    fun userRegister(@RequestBody request: String, @PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restPostHelper(
                restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/register",
                request,
                "URI: /$v/user/register RETURN NULL")
    }

    @PostMapping("/login")
    fun userLogin(@RequestBody request: String, @PathVariable("v") v: String, response: HttpServletResponse): ResponseJSON {
        // Login
        val resp = RestJSONHelper.restPostHelper(
                restTemplate,
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
        return ResponseJSON(0, mapOf("login" to if (loginValid(userLoginCheckHelper(userId, token, v))) 1 else 0))
    }

    @GetMapping("/logout")
    fun logout(
            @CookieValue(UserAPIConfig.cookieUserIdKey, defaultValue = "-1") userId: String?,
            @CookieValue(UserAPIConfig.cookieUserTokenKey, defaultValue = "") token: String?,
            @PathVariable("v") v: String): ResponseJSON {
        val resp = userLoginCheckHelper(userId, token, v)

        if (loginValid(resp)) {
            return RestJSONHelper.restGetHelper(
                    restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/logout/$userId",
                "URI: /$v/user/logout RETURN NULL"
            )
        }
        return ResponseJSON(0, msg = "No login")
    }

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable("id") id: Int, @PathVariable("v") v: String): ResponseJSON {
        return RestJSONHelper.restGetHelper(
                restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/id/$id",
                "URI: /$v/user/logout RETURN NULL"
        )
    }

    private fun userLoginCheckHelper(id: String?, token: String?, v: String): ResponseJSON {
        return RestJSONHelper.restGetHelper(
                restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/token/$id/$token",
                "URI: /$v/user/token RETURN NULL"
        )
    }

    private fun loginValid(resp: ResponseJSON?): Boolean {
        return resp != null && resp.status == 0 && resp.obj == true
    }
}
