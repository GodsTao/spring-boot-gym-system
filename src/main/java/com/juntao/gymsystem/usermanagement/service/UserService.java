package com.juntao.gymsystem.usermanagement.service;

import com.github.pagehelper.Page;
import com.juntao.gymsystem.usermanagement.domain.User;


public interface UserService {
    /**
     * 保存新用户
     * @param user
     */
    public void saveUser(User user,Long roleId);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    public User getUserByUsername(String username);

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    public User getUserById(Long id);

    /**
     * 查找所有用户
     * @return
     */
    public Page<User> getAll(int pageIndex,int pageSize);

    /**
     * 根据用户名模糊查询
     * @param pageIndex
     * @param pageSize
     * @param username
     * @return
     */
    public Page<User> getAllByUsernameLike(int pageIndex,int pageSize,String username);

    /**
     * 更新用户数据
     * @param user
     * @param roleId
     */
    public void updateUser(User user,Long roleId);

    /**
     * 根据id删除用户
     * @param id
     */
    public void deleteUser(Long id);

    /**
     * 保存头像
     * @param username
     * @param avatar
     */
    public void saveAvatar(String username,String avatar);

    /**
     * 忘记密码
     * @param user
     */
    public void updateForgetedPassword(User user);

    /**
     * 更新个人信息
     * @param user
     */
    public void updateProfile(User user);
}
