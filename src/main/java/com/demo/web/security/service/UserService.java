package com.demo.web.security.service;

import com.demo.web.security.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getUserByName(String userName) {
        return new User();
    }
}
