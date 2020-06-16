package com.cynaith.ifile.pojo.resultVo;

/**
 * @author: Cynaith
 **/
public class ResponseVo {
    private Integer status;
    private String msg;
    private Object obj;

    public static ResponseVo build() {
        return new ResponseVo();
    }

    public static ResponseVo ok(String msg) {
        return new ResponseVo(200, msg, null);
    }

    public static ResponseVo ok(String msg, Object obj) {
        return new ResponseVo(200, msg, obj);
    }

    public static ResponseVo error(String msg) {
        return new ResponseVo(500, msg, null);
    }

    public static ResponseVo error(String msg, Object obj) {
        return new ResponseVo(500, msg, obj);
    }

    private ResponseVo() {
    }

    private ResponseVo(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public ResponseVo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseVo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public ResponseVo setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}
