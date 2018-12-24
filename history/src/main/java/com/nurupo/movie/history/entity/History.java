package com.nurupo.movie.history.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class History {
    @Id
    @Column(name = "HID")
    @GeneratedValue
    @JsonProperty("historyId")
    private int historyId;

    @Column(name = "UID", nullable = false)
    @JsonProperty("userId")
    private int userId;

    @Column(name = "MID", nullable = false)
    @JsonProperty("movieId")
    private String movieId;

    @Column(name = "RATING", nullable = false)
    @JsonProperty("rating")
    private float rating;

    @Column(name = "TIMESTAMP", nullable = false)
    @JsonProperty("timestamp")
    private long timestamp;

    public History(int userId, String movieId, float rating, Long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }
}