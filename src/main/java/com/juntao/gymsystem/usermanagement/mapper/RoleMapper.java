package com.juntao.gymsystem.usermanagement.mapper;

import com.juntao.gymsystem.usermanagement.domain.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    /**
     * 根据用户id查询角色
     * @param userId
     * @return
     */
    @Select("select id,name from user_role  u ,role r where u.user_id=#{userId} and u.role_id = r.id")
    List<Role> findByUserId(@Param("userId") Long userId);

    /**
     * 将用户id和角色id保存至uer_role表，实现多对多
     * @param userId
     * @param roleId
     */
    @Insert("insert into user_role(user_id,role_id) value(#{userId},#{roleId})")
    void saveRole(@Param("userId")Long  userId ,@Param("roleId")Long roleId);

    @Update("update user_role set user_id= #{userId},role_id=#{roleId} where user_id=#{userId}")
    void updateRole(@Param("userId") Long userId,@Param("roleId") Long roleId);
}
