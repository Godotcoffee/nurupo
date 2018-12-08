package com.nurupo.movie.users.dao

import com.nurupo.movie.users.entity.User

interface IUserDao {

    fun getAllUsers(): List<User>

    fun getUserByName(name: String): User?

    fun addUser(user: User): Boolean
}