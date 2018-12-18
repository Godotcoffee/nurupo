package com.nurupo.movie.history.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseJSON {
    @JsonProperty("status")
    private int status;

    @JsonProperty("obj")
    private Object obj;

    @JsonProperty("msg")
    private String msg;

    public ResponseJSON() {
        this(0);
    }

    public ResponseJSON(int status) {
        this(status, null);
    }

    public ResponseJSON(int status, Object obj) {
        this(status, obj, null);
    }

    public ResponseJSON(int status, Object obj, String msg) {
        this.status = status;
        this.obj = obj;
        this.msg = msg;
    }
}
