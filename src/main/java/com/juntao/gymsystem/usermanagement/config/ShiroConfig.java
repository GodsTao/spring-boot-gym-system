package com.juntao.gymsystem.usermanagement.config;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 *
 */
@Configuration
public class ShiroConfig {
    /**
     *创建ShiroFilterFactoryBean
     */
    @Bean //添加@Bean才能被spring扫描到
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean =new ShiroFilterFactoryBean();

        //设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //添加Shiro内置过滤器
        /**
         * 常用过滤器：
         * anon:无需认证 便可访问
         * authc:必须认证才能登陆
         * user：使用rememberMe的功能可以直接访问
         * perms：该资源获得资源权限才能访问
         * role：需要相应角色才能访问
         */
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/", "anon");
        filterMap.put("/toLogin","anon");
        filterMap.put("/admin/**", "roles[ROLE_ADMIN]");
        filterMap.put("/static/**","anon");
        filterMap.put("/bower_components/**","anon");
        filterMap.put("/css/**","anon");
        filterMap.put("/js/**","anon");
        filterMap.put("/img/**","anon");
        filterMap.put("/plugins/**","anon");
        filterMap.put("/u/**","authc");
        filterMap.put("/logout","logout");
        //调整跳转的登录页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        //登录成功跳转页面
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }
    /**
     *创建DefaultSecurityManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
//        自定义缓存实现 使用redis
//        defaultWebSecurityManager.setCacheManager(cacheManager());
//        自定义session管理 使用redis
//        defaultWebSecurityManager.setSessionManager(SessionManager());

        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultWebSecurityManager;
    }
    /**
     * 创建一个Realm
     */
    @Bean(name = "userRealm")
    public UserRealm getUserRealm() {
        return new UserRealm();
    }

    /**
     * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");

        //设置为true只能由http访问Cookie,js无法访问，防止xss读取
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");

        //设置cookie生效时间一个小时,单位：秒
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理对象,rememberMe管理器
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    /**
     * FormAuthenticationFilter 过滤器 过滤记住我
     * @return
     */
    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }
}
