package com.nurupo.movie.history.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Xueba {
    @JsonProperty("xueba_name")
    private String name;
    @JsonProperty("xueba_age")
    private int age;
    @JsonProperty("phone")
    private String phone;
}
