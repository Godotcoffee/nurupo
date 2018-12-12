package com.nurupo.movie.users.controller

import com.nurupo.movie.users.bean.UserConfig
import com.nurupo.movie.users.dao.ITokenDao
import com.nurupo.movie.users.dao.IUserDao
import com.nurupo.movie.users.entity.Login
import com.nurupo.movie.users.entity.ResponseJSON
import com.nurupo.movie.users.entity.Token
import com.nurupo.movie.users.entity.User
import com.nurupo.movie.users.tools.IPasswordHash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.Validator
import kotlin.math.max
import kotlin.math.min


@RestController
@RequestMapping("/v1/user")
class UserControllerV1 {
    @Autowired
    lateinit var userConfig: UserConfig

    @Autowired
    lateinit var userDao: IUserDao

    @Autowired
    lateinit var tokenDao: ITokenDao

    @Autowired
    lateinit var userPasswordHash: IPasswordHash<User>

    @PostMapping("/register", produces = ["application/json;charset=utf-8;"])
    fun register(@Valid @RequestBody user: User, bindingResult: BindingResult): ResponseJSON {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseJSON(-1, bindingResult.allErrors, "Valid failed")
            }
            if (userDao.findByUsername(user.username) != null) {
                return ResponseJSON(1, msg = "Username has been registered already")
            }
            if (userDao.findByEmail(user.email) != null) {
                return ResponseJSON(2, msg = "Email has been registered already")
            }
            user.hashedPassword = userPasswordHash.hash(user)
            user.id = 0
            userDao.save(user)
            return ResponseJSON(0, mapOf("userId" to user.id))
        } catch (e: Exception) {
            return ResponseJSON(-255, e.message,  "Exception")
        }
    }

    @PostMapping("/login", produces = ["application/json;charset=utf-8;"])
    fun login(@Valid @RequestBody login: Login, bindingResult: BindingResult): ResponseJSON {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseJSON(-1, bindingResult.allErrors, "Valid failed")
            }
            val userData = userDao.findByUsername(login.username) ?: return ResponseJSON(404, msg = "User not existed")

            if (userData.hashedPassword != userPasswordHash.hash(userData.apply { password = login.password })) {
                return ResponseJSON(401, msg = "Invalid failed")
            }

            val tokenKey = UUID.randomUUID()

            val newToken = Token(0, tokenKey, userData.id, Date(), userConfig.userLoginDuration, false)
            tokenDao.findByUserId(userData.id)?.apply { newToken.id = id }
            tokenDao.save(newToken)
            return ResponseJSON(0, mapOf("userId" to userData.id, "token" to newToken.tokenKey))
        } catch (e: Exception) {
            return ResponseJSON(-255, e.message, "Exception")
        }
    }

    @GetMapping("/token/{userId}/{token}")
    fun loginCheck(@PathVariable("userId") userId: Int, @PathVariable("token") tokenStr: String): ResponseJSON {
        try {
            val uuid = UUID.fromString(tokenStr)
            val check = tokenDao.findByUserId(userId)?.run {
                !logout && uuid == tokenKey && startDate.time + durationDate.toMillis() >= Date().time
            } ?: false
            return ResponseJSON(0, check)
        } catch (e: Exception) {
            return ResponseJSON(-255, e.message, "Exception")
        }
    }

    @GetMapping("/logout/{userId}")
    fun logout(@PathVariable("userId") userId: Int): ResponseJSON {
        try {
            tokenDao.findByUserId(userId)?.apply {
                if (logout) {
                    logout = true
                    tokenDao.save(this)
                }
            }
            return ResponseJSON(0)
        } catch (e: Exception) {
            return ResponseJSON(-255, e.message, "Exception")
        }
    }

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable("id") id: Int): ResponseJSON {
        try {
            return userDao.findById(id).run {
                if (isPresent)
                    ResponseJSON(0, get())
                else
                    ResponseJSON(1, msg = "User doesn't exist")
            }

        } catch (e: Exception) {
            return ResponseJSON(-255, e.message, "Exception")
        }
    }

    @GetMapping("/name/{name}")
    fun getUserByName(@PathVariable("name") username: String): ResponseJSON {
        try {
            return userDao.findByUsername(username)?.let {
                    ResponseJSON(0, it)
            } ?: ResponseJSON(1, msg = "User doesn't exist")
        } catch (e: Exception) {
            return ResponseJSON(-255, e.message, "Exception")
        }
    }

    @GetMapping("/all")
    fun getAllUser(
            @RequestParam("page", defaultValue = "0") page: Int,
            @RequestParam("pageSize", defaultValue = "10") pageSize: Int): ResponseJSON {
        val p = max(page, 0)
        val ps = min(max(pageSize, 1), userConfig.maxPageSize)
        try {
            return ResponseJSON(0, userDao.findAll(PageRequest.of(p, ps)))
        } catch (e: Exception) {
            return ResponseJSON(-255, e.message, "Exception")
        }
    }

    @GetMapping("/init")
    fun initData(): ResponseJSON {
        val list = mutableListOf<User>()
        for (i in 1..100) {

            list.add(User(0, "Xueba$i", "12345", email = "88$i@qq.com")
                    .also { it.hashedPassword = userPasswordHash.hash(it) })
        }
        userDao.saveAll(list.asIterable())
        return ResponseJSON()
    }
}
