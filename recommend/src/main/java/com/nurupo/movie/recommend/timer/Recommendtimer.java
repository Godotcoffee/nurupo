package com.nurupo.movie.recommend.timer;

import com.nurupo.movie.recommend.core.Algorithm;
import com.nurupo.movie.recommend.dao.UserRepository;
import com.nurupo.movie.recommend.entity.User;
import com.nurupo.movie.recommend.core.JavaALS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Recommendtimer {

    @Autowired
    Algorithm algo;

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    private void test() {
        System.out.println("执行定时任务的时间是："+new Date());
        algo.javaALSAlgorithm();
    }
}
