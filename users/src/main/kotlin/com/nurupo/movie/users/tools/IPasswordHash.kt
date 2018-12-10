package com.nurupo.movie.users.tools

interface IPasswordHash<T> {
    fun hash(t: T): String
}