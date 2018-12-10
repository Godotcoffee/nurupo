package com.nurupo.movie.users.controller

import com.nurupo.movie.users.dao.IUserDao
import com.nurupo.movie.users.entity.ResponseJSON
import com.nurupo.movie.users.entity.User
import com.nurupo.movie.users.tools.IPasswordHash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import javax.validation.Valid


@RestController
@RequestMapping("/v1/user")
class UserController {
    @Autowired
    lateinit var userDao: IUserDao

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
            userDao.save(user)
        } catch (e: Exception) {
            return ResponseJSON(2, e.message, msg = "Save Exception")
        }
        return ResponseJSON(0)
    }

    
}
