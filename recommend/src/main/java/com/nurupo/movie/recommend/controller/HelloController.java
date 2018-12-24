package com.nurupo.movie.recommend.controller;

import com.nurupo.movie.recommend.core.Algorithm;
import com.nurupo.movie.recommend.core.JavaALS;
import com.nurupo.movie.recommend.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/recommend", produces = {"application/json;charset=utf-8;"})
public class HelloController {
    @Autowired
    Algorithm algo;

    @RequestMapping("/hello")
    public String hello(){
        algo.javaALSAlgorithm();
        return "";
    }
}
