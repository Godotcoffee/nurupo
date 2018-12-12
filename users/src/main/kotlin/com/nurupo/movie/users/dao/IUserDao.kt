package com.nurupo.movie.users.dao

import com.nurupo.movie.users.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface IUserDao : JpaRepository<User, Int> {
    fun findByUsername(name: String): User?

    fun findByEmail(email: String): User?
}