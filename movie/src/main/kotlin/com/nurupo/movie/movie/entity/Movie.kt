package com.nurupo.movie.movie.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "MOVIES")
data class Movie(
        @Id
        @Column(name = "MID")
        @JsonProperty("id")
        var id: String,

        @Column(name = "MNAME", nullable = false)
        @JsonProperty("movieName")
        var name: String,

        @Column(name = "YEAR", nullable = false)
        var year: Int,

        @Column(name = "GENRES", nullable = false)
        @JsonProperty("genres")
        var genres: String,

        @Column(name = "COVER", nullable = false)
        @JsonProperty("cover")
        var cover: String

//        @Column(name = "RATE", nullable = false)
//        @JsonProperty("rate")
//        var rate: Float,
//
//        @Temporal(TemporalType.TIMESTAMP)
//        @Column(name = "TIMESTAMP", nullable = false)
//        @JsonProperty("timestamp")
//        var timestamp: Long,
//
//        @Column(name = "IMDB_RATE")
//        @JsonProperty("imdbRate")
//        var imdbRate: Float?,
//
//        @Column(name = "TMBD_RATE")
//        @JsonProperty("tmbdRate")
//        var tmdbRate: Float?
)