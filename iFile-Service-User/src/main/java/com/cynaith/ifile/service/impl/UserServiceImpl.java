package com.cynaith.ifile.service.impl;

import com.cynaith.ifile.pojo.domain.User;
import com.cynaith.ifile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: Cynaith
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    CassandraTemplate cassandraTemplate;

    @Override
    public boolean login(String username, String password) {
        User user = cassandraTemplate.selectOne("select * from ifile.user where kind = \'user\' and username = \'"+username+"\' ALLOW FILTERING;", User.class);
        if (password.equals(user.getPassword()))
            return true;
        return false;
    }
}
