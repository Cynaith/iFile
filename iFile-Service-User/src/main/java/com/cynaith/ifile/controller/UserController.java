package com.cynaith.ifile.controller;

import com.alibaba.fastjson.JSONObject;
import com.cynaith.ifile.service.UserService;
import com.cynaith.ifile.util.GetRequestJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLSessionContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: Cynaith
 **/

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public boolean login(HttpServletRequest request, HttpServletResponse response,@RequestBody String json){
        JSONObject object = JSONObject.parseObject(json);
        return userService.login((String)object.get("username"),(String)object.get("password"));
    }

}
