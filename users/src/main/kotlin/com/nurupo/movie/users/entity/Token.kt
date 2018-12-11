package com.nurupo.movie.users.entity

import java.time.Duration
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "TOKENS")
data class Token(
        @Id
        @GeneratedValue
        @Column(name = "TID")
        var id: Int,

        @Column(name = "TOKEN_KEY", nullable = false)
        var tokenKey: UUID,

        @Column(name = "UID", unique = true, nullable = false)
        var userId: Int,

        @Column(name = "S_TIME", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        var startDate: Date,

        @Column(name = "D_TIME", nullable = false)
        var durationDate: Duration
)