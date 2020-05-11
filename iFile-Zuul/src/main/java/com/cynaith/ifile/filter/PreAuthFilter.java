package com.cynaith.ifile.filter;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSONObject;
import com.cynaith.ifile.util.GetRequestJsonUtils;
import com.cynaith.ifile.util.RedisUtil;
import com.cynaith.ifile.vo.UserRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        System.out.print(String.format("send %s request to %s",request.getMethod(),request.getRequestURI()));
        HttpSession session = request.getSession();
        String requestJson = null;
        try {
            requestJson = GetRequestJsonUtils.getRequestJsonString(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * token (name+pass) MD5
         */
        switch (request.getRequestURI()){
            case "api/user/login":
                //如果Redis里SessionID对应的value 与 session里的token相同 通过验证
                if (session.getAttribute("token").equals(RedisUtil.get(session.getId()))){
                    requestContext.setSendZuulResponse(true);
                    requestContext.setResponseStatusCode(200);
                }

                //如果Session未验证通过
                else {
                    //将json转换为UserRequest
                    UserRequest user = JSONObject.parseObject(requestJson,UserRequest.class);
                    Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
                    //密码正确存入token 至 redis <sessionId,token> 与 session <"token",token>
                    if ("true".equals(user.getPassword())){
                        byte[] token = sign.sign((user.getUsername()+user.getPassword()).getBytes());
                        RedisUtil.set(session.getId(),new String(token));
                        session.setAttribute("token",new String(token));
                        requestContext.setSendZuulResponse(true);
                        requestContext.setResponseStatusCode(200);
                    }
                    else{
                        requestContext.setSendZuulResponse(false);
                        requestContext.setResponseStatusCode(401);
                    }
                }
                break;
            case "api/file":
        }
        if ("api/user/login".equals(request.getRequestURI())){




        }
        return null;
    }
}
