/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.web.security.realm;

import com.demo.web.security.model.User;
import com.demo.web.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;


    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        try {
            User user = userService.getUserByName(primaryPrincipal);
            Set<String> roles = new HashSet<>();
            authorizationInfo.setRoles(roles);

            Set<String> permissions = new HashSet<>();
            authorizationInfo.setStringPermissions(permissions);

        } catch (Exception e) {
            log.error("ShiroRealm.doGetAuthorizationInfo: " + e.getMessage());
        }

        return authorizationInfo;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String principal = (String) token.getPrincipal();
        String password = "";
        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword());
        }
        User user = userService.getUserByName(principal);

        if (user == null) {
            throw new UnknownAccountException("未知账号，请重新输入！");// 没找到帐号
        }

        if (!user.getPassword().equals(password)) {
            throw new IncorrectCredentialsException("对不起，您的密码不正确！");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getLoginName(), user.getPassword(), getName());
        return authenticationInfo;
    }

}
