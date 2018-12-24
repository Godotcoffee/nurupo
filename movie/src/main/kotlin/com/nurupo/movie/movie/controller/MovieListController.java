package com.nurupo.movie.movie.controller;

import com.nurupo.movie.movie.bean.MovieConfig;
import com.nurupo.movie.movie.dao.IMovieDao;
import com.nurupo.movie.movie.entity.Movie;
import com.nurupo.movie.movie.entity.ResponseJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/v1/movie", produces = {"application/json;charset=utf-8;"})
public class MovieListController {
    @Autowired
    private IMovieDao movieDao;

    @PostMapping(value = "/add-item")
    public ResponseJSON addMovieItem(@RequestBody Movie movieItem) {
        if (movieDao.findById(movieItem.getId()).isPresent()) {
            return new ResponseJSON(1, null, "The movie already exists");
        }

        movieDao.save(movieItem);
        return new ResponseJSON(0, movieItem);
    }

    @PostMapping(value = "/add-list")
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

    @GetMapping(value = "/filter")
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

    @GetMapping(value = "/id/{movieId}")
    public ResponseJSON getMovieItem(@PathVariable("movieId") String movieId) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (movieDao.findById(movieId).isPresent()) {
            Movie movieItem = movieDao.findById(movieId).get();
            result.put("movie", movieItem);
            return new ResponseJSON(0, result);
        }

        return new ResponseJSON(1, null, "Movie not existed");
    }

    @GetMapping(value = "/ids/{movies}")
    public ResponseJSON getMovieArray(@PathVariable("movies") String movies) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Movie> movieList = new ArrayList<>();
        String[] idArr = movies.split(",");
        for (String movieId : idArr) {
            if (movieDao.findById(movieId).isPresent()) {
                Movie movieItem = movieDao.findById(movieId).get();
                movieList.add(movieItem);
            }
        }
        result.put("movies", movieList);

        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/all")
    public ResponseJSON getAllMovie(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
            @RequestParam(value = "year", defaultValue = "") String year,
            @RequestParam(value = "genres", defaultValue = "") String genres) {
        page = Math.max(page, 0);
        pageSize = Math.min(Math.max(pageSize, 12), MovieConfig.MAX_PAGE_SIZE);
        Map<String, Object> result = new LinkedHashMap<>();

        Page<Movie> moviePage = movieDao.findAllMovieByType(year, genres, PageRequest.of(page, pageSize));
        result.put("movies", moviePage);

        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/type")
    public ResponseJSON getAllType(){
        Map<String, Object> result = new LinkedHashMap<>();
        List<Integer> allYear = movieDao.findDistinctYearType();
        result.put("years", allYear);

        List<String> allGenres = movieDao.findDistinctGenresType();
        Set<String> genresSet = new LinkedHashSet<>();
        allGenres.forEach(item -> {
            String[] genresArr = item.split("\\|");
            genresSet.addAll(Arrays.asList(genresArr));
        });
        result.put("genres", genresSet);

        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/init")
    public ResponseJSON initMovie() {
        Resource resource = new ClassPathResource("data/movies.csv");
        try {
            BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
            List<Movie> movieList = new ArrayList<>();
            Map<String, String> coverMap = getCoverHashMap();
            boolean readFirstLine = false;

            int incorrectNum = 0;
            String line = "";
            while((line = br.readLine()) != null) {
                if (!readFirstLine) {
                    readFirstLine = true;
                    continue;
                }
                String[] tempStr = line.split(",(?! )");
                if (tempStr.length != 3) {
                    incorrectNum++;
                } else {
                    tempStr[1] = tempStr[1].replaceAll("\"", "").trim();
                    String name = tempStr[1].substring(0, tempStr[1].length() - 6);
                    String yearStr = tempStr[1].substring(tempStr[1].length() - 5, tempStr[1].length() - 1);
                    String cover = "https://m.media-amazon.com/images/M/MV5BMDU2ZWJlMjktMTRhMy00ZTA5LWEzNDgtYmNmZTEwZTViZWJkXkEyXkFqcGdeQXVyNDQ2OTk4MzI@._V1_UX182_CR0,0,182,268_AL_.jpg";
                    if (coverMap.containsKey(tempStr[0])) {
                        cover = coverMap.get(tempStr[0]);
                    }
                    int year;
                    try {
                        year = Integer.parseInt(yearStr);
                    } catch (NumberFormatException e) {
                        year = 2018;
                    }
                    Movie newMovie = new Movie(tempStr[0], name, year, tempStr[2], cover);
                    movieList.add(newMovie);
                }
            }
            br.close();
            movieDao.saveAll(movieList);

            return new ResponseJSON(0, null, String.valueOf(incorrectNum));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseJSON(1, null, "file not found");
        } catch(IOException e) {
            e.printStackTrace();
            return new ResponseJSON(2, null, "io exception");
        }
    }

    private Map<String, String> getCoverHashMap() throws IOException {
        Map<String, String> coverMap = new LinkedHashMap<>();
        Resource resource = new ClassPathResource("data/cover.csv");
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
        String line = "";
        while((line = br.readLine()) != null) {
            String[] tempStr = line.split(",(?=\")");
            if (tempStr.length != 2) {
                continue;
            }
            tempStr[1] = tempStr[1].replaceAll("\"", "").trim();
            coverMap.put(tempStr[0], tempStr[1]);
        }

        return coverMap;
    }
}
