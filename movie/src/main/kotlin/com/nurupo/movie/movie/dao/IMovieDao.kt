package com.nurupo.movie.movie.dao

import com.nurupo.movie.movie.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository

interface IMovieDao: JpaRepository<Movie, String> {
    fun findByName(name: String): Movie?
    fun findAllByYear(year: Int): List<Movie>
    fun findAllByGenresContaining(genre: String): List<Movie>
}