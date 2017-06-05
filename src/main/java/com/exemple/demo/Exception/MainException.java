package com.exemple.demo.Exception;

import com.exemple.demo.enums.ResultEnum;

/**
 * Created by Caby on 5/17/17.
 */

public class MainException extends RuntimeException {
    private ResultEnum resultEnum;

    public MainException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }

    public ResultEnum getResultEnum() { return this.resultEnum; }
    public void setResultEnum(ResultEnum resultEnum) { this.resultEnum = resultEnum; }
    @Override
    public String toString() {
        return "MainException{" + "code=" + this.resultEnum.getCode() + ", msg='" + this.resultEnum.getMsg() + "'}";
    }
}
