package com.nurupo.movie.movie.dao

import com.nurupo.movie.movie.entity.Movie
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

interface IMovieDao: JpaRepository<Movie, String> {
    fun findByName(name: String): Movie?

    fun findAllByYear(year: Int): List<Movie>

    fun findAllByGenresContaining(genres: String): List<Movie>

    fun findAllByNameContaining(name: String, pageable: Pageable): Page<Movie>

    @Query("SELECT DISTINCT m.year FROM Movie m ORDER BY m.year")
    fun findDistinctYearType(): List<Int>

    @Query("SELECT DISTINCT m.genres FROM Movie m")
    fun findDistinctGenresType(): List<String>

    @Query("SELECT m FROM Movie m WHERE m.year like CONCAT('%',:yearType,'%') and m.genres like CONCAT('%',:genresType,'%')")
//    fun findAllMovieByType(yearType: Int, genresType: String, pageRequest: PageRequest): Page<Movie>
    fun findAllMovieByType(yearType: String, genresType: String, pageable: Pageable): Page<Movie>

//    fun findAllByYearContainingAndGenresContaining(year: Int, genres: String): List<Movie>
}