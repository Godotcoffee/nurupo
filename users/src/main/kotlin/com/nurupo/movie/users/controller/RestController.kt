package com.nurupo.movie.users.controller

import com.nurupo.movie.users.dao.IUserDao
import com.nurupo.movie.users.entity.ResponseJSON
import com.nurupo.movie.users.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {
    @Autowired
    lateinit var userDao: IUserDao

    @GetMapping("/user/{name}")
    fun getUser(@PathVariable("name") name: String): ResponseJSON {
        val user = userDao.findByUsername(name)
        return ResponseJSON(when(user) {null -> 1; else -> 0}, user)
    }

    @PostMapping("/user/{name}/{password}")
    fun postUser(@PathVariable name: String, @PathVariable password: String): ResponseJSON {
        try {
            userDao.save(User(0, name, password))
        } catch (e: Exception) {
            return ResponseJSON( 1, null, "Failed")
        }
        return ResponseJSON(0, null)
    }

    @GetMapping("/users")
    fun getAllUsers(): ResponseJSON {
        return ResponseJSON(0, userDao.findAll())
    }
}