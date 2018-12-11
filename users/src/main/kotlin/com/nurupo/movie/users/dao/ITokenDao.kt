package com.nurupo.movie.users.dao

import com.nurupo.movie.users.entity.Token
import org.springframework.data.jpa.repository.JpaRepository

interface ITokenDao : JpaRepository<Token, Int> {
    fun findByUserId(id: Int): Token?
}