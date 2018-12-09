package com.nurupo.movie.users.entity

data class ResponseJSON(
        val status: Int = 0,
        val obj: Any?,
        val msg: String? = null
)