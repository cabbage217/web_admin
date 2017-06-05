package com.exemple.demo.utils;

import com.exemple.demo.domain.Result;
import com.exemple.demo.enums.ResultEnum;

/**
 * Created by Caby on 5/17/17.
 */
public class ResultUtil {
    public static Result success(Object obj) {
        return new Result<>(ResultEnum.SUCCESS, obj);
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(ResultEnum resultEnum) {
        return new Result(resultEnum);
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }
}
