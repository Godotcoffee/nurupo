package com.nurupo.movie.gateway.entity

data class ResponseJSON(
        var status: Int = 0,
        var obj: Any? = null,
        var msg: String? = null
)