package com.juntao.gymsystem.usermanagement.mapper;

import com.github.pagehelper.Page;
import com.juntao.gymsystem.usermanagement.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from user where username=#{value}")
    @Results ({
        @Result(property = "id",column = "id"),
        @Result(property = "roles",column = "id",
        many = @Many(select = "com.juntao.gymsystem.usermanagement.mapper.RoleMapper.findByUserId"))
    })
    User findByUsername(String username);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "roles",column="id",
                    many =@Many(select = "com.juntao.gymsystem.usermanagement.mapper.RoleMapper.findByUserId"))
    })
    User findById(Long id);

    @Select("select * from user")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "roles",column="id",
                    many =@Many(select = "com.juntao.gymsystem.usermanagement.mapper.RoleMapper.findByUserId"))
    })
    Page<User> findAll();

    /**
     * 根据用户名模糊查询
     * @return
     */
    @Select("select * from user where username like #{name}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "roles",column="id",
            many =@Many(select = "com.juntao.gymsystem.usermanagement.mapper.RoleMapper.findByUserId"))
    })
    Page<User> findAllByUsernameLike(@Param(value = "name")String username);

    /**
     * 根据id删除用户
     * @param id
     */
    @Delete("delete from user where id=#{id}")
    void deleteById(Long id);

    /**
     * 保存新用户
     * @param user
     * @return
     */
    @Insert("insert into user(id,username,password,email) value(#{id},#{username},#{password},#{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
    Long saveUser(User user);

    /**
     * 更新用户
     * @param user
     * @return
     */
    @Update("update user set password=#{password},email=#{email} where id=#{id}")
    @Options(keyProperty = "id",keyColumn = "id")
    Long updateUser(User user);

    /**
     * 通过邮箱修改密码
     * @param user
     * @return
     */
    @Update("update user set password=#{password} where email=#{email}")
    @Options(keyProperty = "id",keyColumn = "id")
    Long updatePasswordByEmail(User user);


    /**
     * 保存头像
     * @param username
     * @param avatar
     */
    @Update("update user set avatar=#{avatar} where username=#{username}")
    void updateAvatar(@Param(value="username") String username,@Param(value="avatar")String avatar);

    /**
     * 更改个人信息
     * @param user
     */
    @Update("update user set name=#{name},phoneNumber=#{phoneNumber},birthday=#{birthday},college=#{college},major=#{major},classes=#{classes},signature=#{signature} where username =#{username}")
    void updateProfile(User user);
}
