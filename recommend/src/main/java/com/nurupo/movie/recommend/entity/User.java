package com.nurupo.movie.recommend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String userId;
    private String movieId;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
