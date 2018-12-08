package com.nurupo.movie.users.entity

data class ResponseJSON(
        val obj: Any?,
        val ok: Boolean = true,
        val error: String = ""
)