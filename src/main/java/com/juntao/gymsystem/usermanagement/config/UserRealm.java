package com.juntao.gymsystem.usermanagement.config;

import com.juntao.gymsystem.usermanagement.domain.Role;
import com.juntao.gymsystem.usermanagement.domain.User;
import com.juntao.gymsystem.usermanagement.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    /**
     * 执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //登录的时候放入的用户信息
        User user=(User) principalCollection.getPrimaryPrincipal();

        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();

        List<Role> roles = user.getRoles();
        if(!roles.isEmpty()) {
            for (Role role : roles) {
                info.addRole(role.getName());
            }
        }
        return info;
    }

    /**
     * 执行认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //编写shiro判断逻辑
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        User user =userService.getUserByUsername(usernamePasswordToken.getUsername());

        if (user==null){  //用户名不存在
            return null; //shiro抛出一个UnknownAccountException
        }else  {
            //要验证的明文密码
            String plaintext = new String(usernamePasswordToken.getPassword());
            //数据库中的加密后的密文
            String hashed = user.getPassword();

            boolean check = BCrypt.checkpw(plaintext, hashed);
            if(!check) {
                throw new AuthenticationException();
            }
        }
        //判断密码
        return new SimpleAuthenticationInfo(user,usernamePasswordToken.getPassword(),getName());
    }

}
