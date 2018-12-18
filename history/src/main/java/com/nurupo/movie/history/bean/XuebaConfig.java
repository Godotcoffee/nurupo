package com.nurupo.movie.history.bean;

import com.nurupo.movie.history.entity.Xueba;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XuebaConfig {
    @Bean
    public Xueba getSuperXueba() {
        Xueba xueba = new Xueba("Hong Weihui", 10, "10086");
        xueba.setAge((int) (10E5 + 0.1));
        return xueba;
    }
}
