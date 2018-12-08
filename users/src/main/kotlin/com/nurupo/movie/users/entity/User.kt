package com.nurupo.movie.users.entity

import javax.persistence.*

@Entity
@Table(name = "USERS")
data class User(
        @Id
        @GeneratedValue
        @Column(name = "UID")
        val id: Int,

        @Column(name = "UNAME", unique = true)
        val username: String,

        @Column(name = "PASSWD")
        val password: String
)