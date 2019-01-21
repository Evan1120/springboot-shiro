/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.web.security.manager;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author evan
 * @date 2016-5-9
 */
public class CustomizedSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session sn) {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
