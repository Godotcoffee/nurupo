package com.nurupo.movie.users.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*
import javax.validation.constraints.*


data class Login(
        @get:Size(min = 4, max = 16, message = "Length of name must from 4 to 16")
        @get:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name must contain only letters and numbers")
        @JsonProperty("username")
        var username: String = "",

        @get:Size(min = 4, max = 16, message = "Length of password must from 4 to 16")
        @get:Pattern(regexp = "^[a-zA-Z0-9\\t\\n ./<>?;:\"'`!@#\$%^&*()\\[\\]{}_+=|\\\\-~,]+$", message = "Password is bad")
        @JsonProperty("password")
        var password: String = ""
)