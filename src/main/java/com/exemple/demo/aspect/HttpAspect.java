package com.exemple.demo.aspect;

import com.exemple.demo.Exception.MainException;
import com.exemple.demo.enums.ResultEnum;
import com.exemple.demo.sevice.IInterfaceService;
import com.exemple.demo.utils.SessionUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Caby on 5/17/17.
 */

@Aspect
@Component
public class HttpAspect {
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);
    @Autowired
    private IInterfaceService interfaceService;

    @Pointcut("execution(public * com.exemple.demo.controller.InterfaceController.*(..))" +
            "|| execution(public * com.exemple.demo.controller.NavigationController.*(..))" +
            "|| execution(public * com.exemple.demo.controller.RoleController.*(..))" +
            "|| execution(public * com.exemple.demo.controller.UserController.*(..))")
    public void log() {}

    @Pointcut("execution(public * com.exemple.demo.controller.LoginController.*(..))")
    public void logLogin() {}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // log
        Signature signature = joinPoint.getSignature();
        logger.debug(
                "url = {}, uri = {}, method = {}, ip={}, class_method={}, args={}",
                request.getRequestURL(), request.getRequestURI(), request.getMethod(),
                request.getRemoteAddr(), signature.getDeclaringTypeName() + signature.getName(),
                joinPoint.getArgs()
        );
        // end log

        // 权限检查
        if (!SessionUtil.isSessionValid(request)) {
            throw new MainException(ResultEnum.ERR_INVALID_SESSION);
        }
        Integer uid = SessionUtil.getUidWithRequest(request);
        if (uid == null || !interfaceService.isUserHasPermissionToInterface(uid, request.getRequestURI())) {
            throw new MainException(ResultEnum.ERR_NO_PERMISSION);
        }
    }

    @AfterReturning(returning = "obj", pointcut = "log()")
    public void doAfterReturning(Object obj) {
        logger.debug("response={}", obj != null ? obj.toString() : "null");
    }

    @Before("logLogin()")
    public void beforeLogin(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // log
        Signature signature = joinPoint.getSignature();
        logger.debug(
                "LOGIN, url = {}, uri = {}, method = {}, ip={}, class_method={}, args={}",
                request.getRequestURL(), request.getRequestURI(), request.getMethod(),
                request.getRemoteAddr(), signature.getDeclaringTypeName() + signature.getName(),
                joinPoint.getArgs()
        );
        // end log
    }

    @AfterReturning(returning = "obj", pointcut = "logLogin()")
    public void afterLogin(Object obj) {
        logger.info("login result = {}", obj != null ? obj.toString() : "null");
    }
}
