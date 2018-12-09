package com.nurupo.movie.spark.controller;

import com.nurupo.movie.spark.core.CountNum;
import com.nurupo.movie.spark.entity.ResponseJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.tools.nsc.doc.html.page.JSONObject;

import java.util.List;

@RestController
public class SparkController {
    @Autowired
    private CountNum countNum;

    private static class Args {
        public List<Integer> list;
    }

    @PostMapping(name = "/sum", produces = {"application/json;charset=utf-8;"})
    public ResponseJSON forTest(@RequestBody Args args) {
        if (args.list == null) {
            return new ResponseJSON(1, null, "List is null");
        }
        int result = countNum.solve(JavaConverters.asScalaIteratorConverter(args.list.iterator()).asScala().toSeq());
        return new ResponseJSON(0, result);
    }
}
