package com.juntao.gymsystem.usermanagement.mapper;

import com.juntao.gymsystem.usermanagement.domain.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper {
    @Select("select * from menu where name like #{name}")
    List<Menu> findMenuByName(@Param("name") String name);
}
