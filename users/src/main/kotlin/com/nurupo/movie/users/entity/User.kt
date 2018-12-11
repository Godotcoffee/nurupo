package com.nurupo.movie.users.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "USERS")
data class User(
        @Id
        @GeneratedValue
        @Column(name = "UID")
        @JsonProperty("id")
        var id: Int = 0,

        @Column(name = "UNAME", unique = true, nullable = false)
        @get:Size(min = 4, max = 16, message = "Length of name must from 4 to 16")
        @get:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name must contain only letters and numbers")
        @JsonProperty("username")
        var username: String = "",

        @Transient
        @get:Size(min = 4, max = 16, message = "Length of password must from 4 to 16")
        @get:Pattern(regexp = "^[a-zA-Z0-9\\t\\n ./<>?;:\"'`!@#\$%^&*()\\[\\]{}_+=|\\\\-~,]+$", message = "Password is bad")
        @JsonProperty("password")
        var password: String = "",

        @Column(name = "PASSWD", nullable = false)
        @JsonIgnore
        var hashedPassword: String = "",

        @Column(name = "EMAIL")
        @get:NotBlank(message = "Can not be empty")
        @get:Email(message = "Check your email address again!")
        @JsonProperty("email")
        var email: String? = null,

        @Column(name = "STATUS")
        @JsonProperty("userStatus")
        var userStatus: Int? = null,

        @Column(name = "RDATE")
        @Temporal(TemporalType.TIMESTAMP)
        @JsonIgnore
        var registerDate: Date = Date()
)