package com.nurupo.movie.history.controller;

import com.nurupo.movie.history.bean.HistoryConfig;
import com.nurupo.movie.history.dao.IHistoryDAO;
import com.nurupo.movie.history.entity.History;
import com.nurupo.movie.history.entity.ResponseJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/v1/history", produces = {"application/json;charset=utf-8;"})
public class HistoryListController {
    @Autowired(required = true)
    private IHistoryDAO historyDAO;

    @PostMapping(value = "/add-item")
    public ResponseJSON addHistoryItem(@RequestBody History historyItem) {
        //userId和movieId相加为historyId
        String historyId = historyItem.getUserId() + historyItem.getMovieId();
        if (historyDAO.findById(historyId).isPresent()) {
            return new ResponseJSON(1, null, "The history already exists");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        historyItem.setTimestamp(timestamp);
        historyItem.setHistoryId(historyId);
        historyDAO.save(historyItem);
        return new ResponseJSON(0, historyItem);
    }

    @PostMapping(value = "/add-list")
    public ResponseJSON addHistoryList(@RequestBody History[] historyList) {
        List<History> newHistoryList = new ArrayList<>();
        long timestamp = System.currentTimeMillis() / 1000;
        String historyId;
        for (History historyItem: historyList) {
            historyId = historyItem.getUserId() + historyItem.getMovieId();
            if (!historyDAO.findById(historyId).isPresent()) {
                historyItem.setTimestamp(timestamp);
                historyItem.setHistoryId(historyId);
                newHistoryList.add(historyItem);
                historyDAO.save(historyItem);
            }
        }
        if (newHistoryList.size() == 0) {
            return new ResponseJSON(1, null, "All histories already exist");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", newHistoryList.size());
        result.put("histories", newHistoryList);
        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/item")
    public ResponseJSON getHistoryItem(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") String movieId
    ) {
        String historyId = userId + movieId;
        if (!historyDAO.findById(historyId).isPresent()) {
            return new ResponseJSON(1, null, "The history doesn't exist");
        }
        History historyItem = historyDAO.findByUserIdAndMovieId(userId, movieId);
        return new ResponseJSON(0, historyItem);
    }

    @GetMapping(value = "/page-userId-timestamp")
    public ResponseJSON getPageByUserIdOrderByTimestamp(
            @RequestParam("userId") int userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize
    ) {
        page =Math.max(page, 0);
        pageSize = Math.min(Math.max(pageSize, 10), HistoryConfig.MAX_PAGE_SIZE);
        Map<String, Object> result = new LinkedHashMap<>();

        Page<History> historyPage = historyDAO.findAllByUserIdOrderByTimestamp(userId, PageRequest.of(page, pageSize));
        result.put("historyPage", historyPage);
        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/page-userId-rating")
    public ResponseJSON getPageByUserIdOrderByRating(
            @RequestParam("userId") int userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize
    ) {
        page = Math.max(page, 0);
        pageSize = Math.min(Math.max(pageSize, 10), HistoryConfig.MAX_PAGE_SIZE);
        Map<String, Object> result = new LinkedHashMap<>();

        Page<History> historyPage = historyDAO.findAllByUserIdOrderByRating(userId, PageRequest.of(page, pageSize));
        result.put("historyPage", historyPage);
        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/page-movieId-timestamp")
    public ResponseJSON getPageByMovieIdOrderByTimestamp(
            @RequestParam("movieId") String movieId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize
    ) {
        page = Math.max(page, 0);
        pageSize = Math.min(Math.max(pageSize, 10), HistoryConfig.MAX_PAGE_SIZE);
        Map<String, Object> result = new LinkedHashMap<>();

        Page<History> historyPage = historyDAO.findAllByMovieIdOrderByTimestamp(movieId, PageRequest.of(page, pageSize));
        result.put("historyPage", historyPage);
        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/all")
    public ResponseJSON getAllHistory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize
    ) {
        page = Math.max(page, 0);
        pageSize = Math.min(Math.max(pageSize, 10), HistoryConfig.MAX_PAGE_SIZE);
        Map<String, Object> result = new LinkedHashMap<>();

        Page<History> historyPage = historyDAO.findAll(PageRequest.of(page, pageSize));
        result.put("historyPage", historyPage);
        return new ResponseJSON(0, result);
    }

    @GetMapping(value = "/init")
    public ResponseJSON initHistory() {
        Resource resource = new ClassPathResource("data/ratings.csv");
        try {
            File file = resource.getFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<History> historyList = new ArrayList<>();

            int incorrectNum = 0;
            String line = "";
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] tempStr = line.split(",");
                if (tempStr.length != 4) {
                    incorrectNum++;
                } else {
                    History historyItem = new History(Integer.parseInt(tempStr[0]), tempStr[1], Float.parseFloat(tempStr[2]), Long.parseLong(tempStr[3]));
                    historyList.add(historyItem);
                }
            }
            br.close();
            historyDAO.saveAll(historyList);
            return new ResponseJSON(0, null, String.valueOf(incorrectNum));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseJSON(1, null);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseJSON(2, null);
        }
    }
}
