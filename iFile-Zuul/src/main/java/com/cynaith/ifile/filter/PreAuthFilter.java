package com.cynaith.ifile.filter;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSONObject;
import com.cynaith.ifile.util.GetRequestJsonUtils;
import com.cynaith.ifile.util.IpUtil;
import com.cynaith.ifile.util.RedisUtil;
import com.cynaith.ifile.util.SessionUtil;
import com.cynaith.ifile.vo.UserRequest;
import com.github.cynaith.jedis.Ledis;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.HttpCookie;

/**
 * @author: Cynaith
 **/
@Component
public class PreAuthFilter extends ZuulFilter {

    //过滤器类型
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    // 判断执行顺序，顺序越小，越靠前拦截
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
    }


    //判断是否应该被拦截
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (request.getMethod().equals("OPTIONS")) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        // 请求携带的参数
        String requestJson = null;
        try {
            requestJson = GetRequestJsonUtils.getRequestJsonString(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 请求URI
        String uri = request.getRequestURI();
        // 客户端Cookie
        Cookie[] cookie = request.getCookies();
        // 会话Session
        HttpSession session = request.getSession();
        // 客户端ip
        String ipAddress = IpUtil.getIpAddress(request);
        // 按照请求体存全局Session 至 Redis-Set
        SessionUtil.saveSessionId(ipAddress,session.getId());
        // 如果客户端禁用Cookie
        if (cookie==null){

        }
        // 如果Cookie未被禁用
        else {
            String sessionIdInCookie = "";
            // 获取种在Cookie下的SessionID
            for (Cookie c:cookie){
                if (c.getName().equals("iFile")){
                    sessionIdInCookie = c.getValue();
                }
            }
            //判断Redis-Set是否存在ip
           if (SessionUtil.haveIpAddress(ipAddress)){
               // 相同ip登陆
               // 获取该ip存储的SessionId
               String SessionId = SessionUtil.getSessionId(ipAddress);
               //如果SessionId与Redis存储的Session相同 请求通过  (可理解为用户该会话期间第n>1次访问系统)
               if (session.getId().equals(SessionId)){
                   // 继续执行
               }else {
                    // 如果当前SessionId不在Redis中 (可理解为用户该会话期间第一次访问系统)

                   // 检查SessionID是否在Redis中
                    if (SessionUtil.haveSessionId(sessionIdInCookie)){
                        // 如果SessionID存在Redis中
                        // 将旧SessionID替换为新SessionID
                        // 1. 将Redis中SessionID更改为现SessionID
                        SessionUtil.saveSessionId(ipAddress,sessionIdInCookie);
                        // 2. 将Cookie中SessionID更新为现SessionID
                        Cookie sessionCookie = new Cookie("iFile",sessionIdInCookie);
                        sessionCookie.setMaxAge(30*24*60*60);
                        sessionCookie.setComment("iFile-SessionId");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("text/html;charset=UTF-8");
                        response.addCookie(sessionCookie);
                        // 3. 请求通过
                        requestContext.setResponse(response);
                        // 继续执行
                    }
               }
           }
           // 不同ip登陆
           else {
                // 1. 根据Cookie中的<iFile,SessionID> 获取SessionID 找到了 -> 2. 找不到 -> 跳转登陆
                // 2. 将Redis Set<ip,SessionID> 中 ip修改为现ip
               if (SessionUtil.haveSessionId(sessionIdInCookie)){
                    SessionUtil.renameIpBySessionId(ipAddress,sessionIdInCookie);
                    // 继续执行
               }else {
                   requestContext.setSendZuulResponse(false);
                   requestContext.setResponseStatusCode(401);
               }
           }
        }


        switch (uri){
            case "/api/user/login":
                requestContext.setSendZuulResponse(true);
                requestContext.setResponseStatusCode(200);
                break;
            case "api/file":
        }
        return null;
    }



}
