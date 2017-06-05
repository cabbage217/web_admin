package com.exemple.demo.utils;

import com.exemple.demo.Const.SessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Caby on 2017-05-26 5:25 PM.
 */

public class SessionUtil {
    /**
     * 从request中获取uid
     * @param request HttpServletRequest
     * @return Integer || null
     */
    static public Integer getUidWithRequest(HttpServletRequest request) {
        Integer uid = null;
        if (request == null) {
            return null;
        }
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }
        Object uidObj = session.getAttribute(SessionKey.Uid);
        if (uidObj == null) {
            return null;
        }
        try {
            uid = Integer.parseInt(uidObj.toString());
        } catch (Exception exc) {
            // do nothing
        }
        return uid;
    }

    /**
     * 检查session是否有效
     * @param request HttpServletRequest
     * @return boolean
     */
    static public boolean isSessionValid(HttpServletRequest request) {
        Integer uid = SessionUtil.getUidWithRequest(request);
        return uid != null && uid > 0;
    }

    /**
     * 重置session
     * @param request HttpServletRequest
     * @return boolean
     */
    static public boolean resetSession(HttpServletRequest request) {
        if (request == null) {
            return true;
        }
        HttpSession session = request.getSession();
        if (session == null) {
            return true;
        }
        session.invalidate();
        return true;
    }

    /**
     * 为uid创建session
     * @param uid Integer
     * @param request HttpServletRequest
     * @return boolean
     */
    static public boolean createSession(Integer uid, HttpServletRequest request) {
        if (uid == null || request == null) {
            return false;
        }
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        session = request.getSession(true);
        if (session == null) {
            return false;
        }
        session.setAttribute(SessionKey.Uid, uid);
        session.setMaxInactiveInterval(SessionKey.ExpireDuration); // one hour
        return true;
    }
}
