package com.nurupo.movie.history.controller;

import com.nurupo.movie.history.entity.ResponseJSON;
import com.nurupo.movie.history.entity.Xueba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tester {
    private final Xueba xueba;

    @Autowired
    public Tester(Xueba xueba) {
        this.xueba = xueba;
    }

    @GetMapping("/xueba")
    public ResponseJSON test() {
        ResponseJSON json = new ResponseJSON();
        json.setObj(xueba);
        return json;
    }
}
