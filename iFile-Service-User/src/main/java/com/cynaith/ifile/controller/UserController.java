package com.cynaith.ifile.controller;

import com.cynaith.ifile.Redis.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author: Cynaith
 **/

@RestController
public class UserController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("iFile-username");
        String password = request.getParameter("iFile-password");
        String token = name+":"+password;
        RedisUtil.set("token",token);
        Cookie cookie = new Cookie("token",token);
        response.addCookie(cookie);
        return token;
    }

}
