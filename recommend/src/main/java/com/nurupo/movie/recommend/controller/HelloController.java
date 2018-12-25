package com.nurupo.movie.recommend.controller;

import com.nurupo.movie.recommend.core.Algorithm;
import com.nurupo.movie.recommend.core.JavaALS;
import com.nurupo.movie.recommend.dao.UserRepository;
import com.nurupo.movie.recommend.entity.ResponseJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/recommend", produces = {"application/json;charset=utf-8;"})
public class HelloController {
    @Autowired
    Algorithm algo;

    @GetMapping("/task")
    public ResponseJSON hello(){
        boolean success = algo.javaALSAlgorithm();
        return new ResponseJSON(success ? 0 : 1);
    }
}
