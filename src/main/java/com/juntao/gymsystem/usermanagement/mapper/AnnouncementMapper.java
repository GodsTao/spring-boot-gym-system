package com.juntao.gymsystem.usermanagement.mapper;

import com.juntao.gymsystem.usermanagement.domain.Announcement;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementMapper {

    @Insert("insert into announcement(content,create_time,user_id) value(#{content},#{createTime},#{userId})")
    void saveAnnouncement(Announcement announcement);

    @Update("update announcement set content=#{content},create_time=#{createTime},user_id=#{userId} where id= '1'")
    void updateAnnouncement(Announcement announcement);

    @Select("select * from announcement where user_id =#{userId}")
    Announcement findAnnouncement(@Param("userId") Long userId);

    @Select("select * from announcement")
    @Results({
            @Result(column = "user_id",property = "userId"),
            @Result(column="create_time",property = "createTime")
    })
    Announcement findAll();

}
