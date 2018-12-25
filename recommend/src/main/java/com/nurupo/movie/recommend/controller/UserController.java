package com.nurupo.movie.recommend.controller;

import com.nurupo.movie.recommend.dao.UserRepository;
import com.nurupo.movie.recommend.entity.ResponseJSON;
import com.nurupo.movie.recommend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/recommend", produces = {"application/json;charset=utf-8;"})
public class UserController {
    @Autowired
    private UserRepository userRepository;
    /**
     * 根据name获取student
     * @param userId
     * @return
     */
    @GetMapping("/id/{userId}")
    public ResponseJSON getStudentById(@PathVariable("userId") String userId){
        List<User> answer = userRepository.findAllByUserId(userId);
        return new ResponseJSON(answer != null ? 0 : -1, answer);
    }

}
