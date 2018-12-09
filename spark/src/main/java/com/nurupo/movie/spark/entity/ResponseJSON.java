package com.nurupo.movie.spark.entity;

public class ResponseJSON {
    private int status = 0;
    private Object obj = null;
    private String msg = null;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
