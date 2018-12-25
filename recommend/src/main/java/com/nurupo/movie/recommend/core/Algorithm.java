package com.nurupo.movie.recommend.core;

import com.nurupo.movie.recommend.dao.UserRepository;
import com.nurupo.movie.recommend.entity.ResponseJSON;
import com.nurupo.movie.recommend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class Algorithm {
    @Autowired
    JavaALS javaALS;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public boolean javaALSAlgorithm() {
        File file = null;
        try {
            file = getHistoryDataFile();
            if (file != null) {
                List<User> list = javaALS.getRecommend(file.getAbsolutePath());
                if (list != null && list.size() > 0) {
                    userRepository.deleteAll();
                    userRepository.saveAll(list);

                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    private File getHistoryDataFile() {
        int page = 0;
        int pageSize = 1000;
        List<List<Object>> data = new ArrayList<>();

        while (true) {
            //System.out.println("page=" + page);
            ResponseJSON resp = restTemplate.getForObject(
                    String.format("http://%s/v1/history/all?page=%d&pageSize=%d", "nurupo-movie-history", page++, pageSize),
                    ResponseJSON.class
            );
            if (resp == null || resp.getStatus() != 0) {
                break;
            }

            Object obj = resp.getObj();
            if (obj instanceof Map) {
                Object historyPage = ((Map) obj).get("historyPage");
                if (historyPage instanceof Map) {
                    Object list = ((Map) historyPage).get("content");
                    Object last = ((Map) historyPage).get("last");

                    if (list instanceof List) {
                        if (((List) list).size() == 0) {
                            break;
                        }
                        for (Object history : (List) list) {
                            if (history instanceof Map) {
                                Map m = (Map) history;
                                List<Object> item = new ArrayList<>();
                                item.add(m.get("userId"));
                                item.add(m.get("movieId"));
                                item.add(m.get("rating"));
                                item.add(m.get("timestamp"));
                                data.add(item);
                            }
                        }
                    }

                    if (!(last instanceof Boolean) || (Boolean) last) {
                        break;
                    }
                }
            }
        }

        File file = null;
        try {
            file = File.createTempFile("com.nurupo.movie.history", ".txt");
            try (PrintWriter pw = new PrintWriter(file)) {
                for (List<Object> list : data) {
                    if (list.size() != 4) { continue; }
                    pw.println(String.format("%s,%s,%s,%s", list.get(0).toString(), list.get(1).toString(), list.get(2).toString(), list.get(3).toString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(file.getAbsolutePath());

        return file;
    }
}
