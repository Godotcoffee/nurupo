package com.nurupo.movie.recommend.dao;

import com.nurupo.movie.recommend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>{
    /**
     * 根据name查询
     * @param userId
     * @return
     */
    public List<User> findByUserId(String userId);
}