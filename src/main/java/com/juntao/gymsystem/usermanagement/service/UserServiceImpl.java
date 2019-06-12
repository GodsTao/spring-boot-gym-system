package com.juntao.gymsystem.usermanagement.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.juntao.gymsystem.usermanagement.domain.User;
import com.juntao.gymsystem.usermanagement.mapper.RoleMapper;
import com.juntao.gymsystem.usermanagement.mapper.UserMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final static long ROLE_USER_ID= 2L;
    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Transactional
    @Override
    public void saveUser(User user,Long roleId) {
        String password =  BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());  //加密
        user.setPassword(password);
        userMapper.saveUser(user);
        Long id= user.getId();
        User result=userMapper.findById(id);
        if(roleId ==null) {
            roleMapper.saveRole(result.getId(),ROLE_USER_ID);
        }else{
            roleMapper.saveRole(result.getId(),roleId);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public Page<User> getAll(int pageIndex,int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        return userMapper.findAll();}

    @Override
    public Page<User> getAllByUsernameLike(int pageIndex,int pageSize,String username) {
        PageHelper.startPage(pageIndex, pageSize);
        username = "%"+username+"%";
        return userMapper.findAllByUsernameLike(username);
    }

    @Transactional
    @Override
    public void updateUser(User user, Long roleId) {
        Pattern BCRYPT_PATTERN = Pattern
                .compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
        if(!BCRYPT_PATTERN.matcher(user.getPassword()).matches()) {  //如果不像是BCRYT编码就加密
            String password =  BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());  //加密
            user.setPassword(password);
        }
        userMapper.updateUser(user);
        Long userId= user.getId();
        roleMapper.updateRole(userId,roleId);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void saveAvatar(String username, String avatar) {
        userMapper.updateAvatar(username,avatar);
    }

    @Transactional
    @Override
    public void updateForgetedPassword(User user) {
        String password =  BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());  //加密
        user.setPassword(password);
        userMapper.updatePasswordByEmail(user);
    }

    @Transactional
    @Override
    public void updateProfile(User user) {
        userMapper.updateProfile(user);
    }

}
