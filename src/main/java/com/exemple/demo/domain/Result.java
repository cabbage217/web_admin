package com.exemple.demo.domain;

import com.exemple.demo.enums.ResultEnum;

/**
 * Created by Caby on 5/17/17.
 */

public class Result<T> {
    private Integer code;
    private String msg;
    private T content;

    public Result(Integer code, String msg, T content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code) {
        this.code = code;
    }

    public Result (ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public Result (ResultEnum resultEnum, T content) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.content = content;
    }

    public Integer getCode() { return this.code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return this.msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getContent() { return this.content; }
    public void setContent(T content) { this.content = content; }

    @Override
    public String toString() {
        return (new StringBuilder("Result{"))
                .append(" code=").append(this.getCode())
                .append(", msg=").append(this.getMsg())
                .append(", content=").append(this.getContent() != null ? this.getContent() : "")
                .toString();
    }
}
