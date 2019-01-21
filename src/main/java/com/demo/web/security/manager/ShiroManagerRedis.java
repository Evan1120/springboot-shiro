/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.web.security.manager;

import com.demo.web.security.redis.RedisCacheManager;
import com.demo.web.security.redis.RedisManager;
import com.demo.web.security.redis.RedisSessionDAO;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author evan
 */
public class ShiroManagerRedis {

    @Bean(name = "redisManager")
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setExpire(60 * 60 * 24 * 30);
        return redisManager;
    }

    @Bean(name = "shiroCacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedisManager redisManager) {
        RedisCacheManager em = new RedisCacheManager();
        em.setRedisManager(redisManager);
        return em;
    }

    @Bean(name = "sessionDAO")
    public SessionDAO sessionDAO(RedisManager redisManager) {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setSessionIdGenerator(new CustomizedSessionIdGenerator());
        sessionDAO.setRedisManager(redisManager);
        return sessionDAO;
    }

    @Bean(name = "sessionManager")
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setGlobalSessionTimeout(30 * 24 * 3600 * 1000);
        sessionManager.setSessionDAO(sessionDAO);
        return sessionManager;
    }

    @Bean(name = "securityManager")
    public DefaultSecurityManager securityManager(org.apache.shiro.session.mgt.SessionManager sessionManager, CacheManager cacheManager) {
        DefaultSecurityManager sm = new DefaultWebSecurityManager();
        sm.setSessionManager(sessionManager);
        sm.setCacheManager(cacheManager);
        return sm;
    }

    @Bean(name = "authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
