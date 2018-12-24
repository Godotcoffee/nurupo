package com.nurupo.movie.recommend.core;

import com.nurupo.movie.recommend.dao.UserRepository;
import com.nurupo.movie.recommend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Algorithm {
    @Autowired
    JavaALS javaALS;

    @Autowired
    UserRepository userRepository;

    public void javaALSAlgorithm() {
        try {
            userRepository.deleteAll();
            List<User> list = javaALS.getRecommend();
            for (User u : list) {
                userRepository.save(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
