package com.nurupo.movie.history.dao;

import com.nurupo.movie.history.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IHistoryDAO extends JpaRepository<History, String> {
    History findByUserIdAndMovieId(int userId, String movieId);

    @Query("SELECT t FROM #{#entityName} t WHERE t.userId = ?1 ORDER BY t.timestamp DESC")
    Page<History> findAllByUserIdOrderByTimestamp(int userId, Pageable pageable);

    @Query("SELECT t FROM #{#entityName} t WHERE t.userId = ?1 ORDER BY t.rating DESC")
    Page<History> findAllByUserIdOrderByRating(int userId, Pageable pageable);

    @Query("SELECT t FROM #{#entityName} t WHERE t.movieId = ?1 ORDER BY t.timestamp DESC")
    Page<History> findAllByMovieIdOrderByTimestamp(String movieId, Pageable pageable);
}
