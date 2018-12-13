package com.nurupo.movie.movie.controller;

import com.nurupo.movie.movie.bean.MovieConfig;
import com.nurupo.movie.movie.dao.IMovieDao;
import com.nurupo.movie.movie.entity.Movie;
import com.nurupo.movie.movie.entity.ResponseJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/movie")
public class MovieListController {
    @Autowired
    private IMovieDao movieDao;

    @PostMapping(value = "/add-item", produces = {"application/json;charset=utf-8;"})
    public ResponseJSON addMovieItem(@RequestBody Movie movieItem) {
        if (movieDao.findById(movieItem.getId()).isPresent()) {
            return new ResponseJSON(1, null, "The movie already exists");
        }

        movieDao.save(movieItem);
        return new ResponseJSON(0, movieItem);
    }

    @PostMapping(value = "/add-list", produces = {"application/json;charset=utf-8;"})
    public ResponseJSON addMovieList(@RequestBody Movie[] movies) {
        List<Movie> newMovieList = new ArrayList<>();
        for (Movie movieItem : movies) {
            if (!movieDao.findById(movieItem.getId()).isPresent()) {
                newMovieList.add(movieItem);
                movieDao.save(movieItem);

            }
        }
        if (newMovieList.size() == 0) {
            return new ResponseJSON(1, null, "All movies already exist");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", newMovieList.size());
        result.put("movies", newMovieList);
        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/filter", produces = {"application/json;charset=utf-8;"})
    public ResponseJSON filterMovie(@RequestParam("type") int type, @RequestParam("key") String key) {
        // type为1时是genres，为2时是year
        Map<String, Object> result = new LinkedHashMap<>();
        String msg = "";
        boolean flag = false;

        List<Movie> filterMovieList = new ArrayList<>();
        if (type == 1) {
            filterMovieList = movieDao.findAllByGenresContaining(key);
            flag = true;
        }
        if (type == 2) {
            try {
                int year = Integer.parseInt(key);
                filterMovieList = movieDao.findAllByYear(year);
                flag = true;
            } catch (NumberFormatException e) {
                msg = "This 'key' needs to be an integer";
                System.out.println(e);
            }
        }
        if (flag) {
            result.put("count", filterMovieList.size());
            result.put("movies", filterMovieList);
            return new ResponseJSON(0, result);
        }

        return new ResponseJSON(1, null, msg);
    }

    @GetMapping(value = "/all", produces = {"application/json;charset=utf-8;"})
    public ResponseJSON getAllMovie(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize) {
        page = Math.max(page, 0);
        pageSize = Math.min(Math.max(pageSize, 10), MovieConfig.MAX_PAGE_SIZE);
        Map<String, Object> result = new LinkedHashMap<>();


        Page<Movie> moviePage = movieDao.findAll(PageRequest.of(page, pageSize));
        result.put("movies", moviePage);

        return new ResponseJSON(0, result);
    }
}