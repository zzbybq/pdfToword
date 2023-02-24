package com.example.pdftoword.domain;

import java.io.Serializable;

public class Response<T> implements Serializable {

    private static final long serialVersionUID = 7105926613643292547L;

    private String flag;
    private String code;
    private String msg;

    private T data;

    public Response() {

    }

    public Response<T> success() {
        this.flag = RespFlagEnum.SUCCESS.getCode();
        return this;
    }

    public Response<T> success(T data) {
        this.flag = RespFlagEnum.SUCCESS.getCode();
        this.data = data;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
