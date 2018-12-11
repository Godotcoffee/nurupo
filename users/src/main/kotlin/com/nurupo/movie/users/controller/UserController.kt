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
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/v1/user")
class UserController {
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
        if (bindingResult.hasErrors()) {
            return ResponseJSON(-1, bindingResult.allErrors, "Valid failed")
        }
        if (userDao.findByUsername(user.username) != null) {
            return ResponseJSON(1, msg = "Username has been registered")
        }
        try {
            user.hashedPassword = userPasswordHash.hash(user)
            user.id = 0
            userDao.save(user)
        } catch (e: Exception) {
            return ResponseJSON(2, e.message, msg = "Save Exception")
        }
        return ResponseJSON(0)
    }

    @PostMapping("/login", produces = ["application/json;charset=utf-8;"])
    fun loginCheck(@Valid @RequestBody login: Login, bindingResult: BindingResult): ResponseJSON {
        if (bindingResult.hasErrors()) {
            return ResponseJSON(-1, bindingResult.allErrors, "Valid failed")
        }

        val userData = userDao.findByUsername(login.username) ?: return ResponseJSON(404, msg = "User not existed")

        if (userData.hashedPassword != userPasswordHash.hash(userData.apply { password = login.password })) {
            return ResponseJSON(401, msg = "Invalid failed")
        }

        val tokenKey = UUID.randomUUID()

        val newToken = Token(0, tokenKey, userData.id, Date(), userConfig.USER_LOGIN_VALID_DURATION)
        tokenDao.findByUserId(userData.id)?.apply { newToken.id = id }
        tokenDao.save(newToken)

        return ResponseJSON(0, mapOf("userId" to userData.id, "token" to newToken))
    }
}
