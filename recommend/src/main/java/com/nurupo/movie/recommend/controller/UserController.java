package com.nurupo.movie.recommend.controller;

import com.nurupo.movie.recommend.dao.UserRepository;
import com.nurupo.movie.recommend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    /**
     * 根据name获取student
     * @param userId
     * @return
     */
    @GetMapping("/findUser/{userId}")
    public List<User> getStudentById(@PathVariable("userId") String userId){
        return userRepository.findByUserId(userId);
    }

}
