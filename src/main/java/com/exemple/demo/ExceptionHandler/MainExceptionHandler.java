package com.exemple.demo.ExceptionHandler;

import com.exemple.demo.Exception.MainException;
import com.exemple.demo.domain.Result;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by Caby on 5/17/17.
 */

@RestControllerAdvice
public class MainExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(MainExceptionHandler.class);

    @ExceptionHandler(value = MainException.class)
    @ResponseBody
    public Result mainHandle(MainException exception) {
        logger.warn("[主要异常] - {}", exception.toString());
        return ResultUtil.error(exception.getResultEnum());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result defaultHandle(Exception exception) {
        StringBuilder stackTrace = new StringBuilder();
        StackTraceElement[] elements = exception.getStackTrace();
        for (StackTraceElement element : elements) {
            stackTrace.append(element.toString()).append("\n");
        }
        logger.error("[系统异常] - {}\n\tStack Trace: {}", exception.getMessage(), stackTrace.toString());
        return ResultUtil.error(ResultEnum.ERR_UNKNOWN);
    }
}
