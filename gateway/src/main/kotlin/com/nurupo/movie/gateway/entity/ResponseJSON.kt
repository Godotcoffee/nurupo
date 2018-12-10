package com.nurupo.movie.gateway.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseJSON(
        @JsonProperty("status")
        var status: Int = 0,

        @JsonProperty("obj")
        var obj: Any? = null,

        @JsonProperty("msg")
        var msg: String? = null
)