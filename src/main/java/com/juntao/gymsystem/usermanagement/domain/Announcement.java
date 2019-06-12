package com.juntao.gymsystem.usermanagement.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Announcement implements Serializable {

    private long id;

    private String content;

    private String createTime;

    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Announcement(String content, String createTime, long userId) {
        this.content = content;
        this.createTime = createTime;
        this.userId = userId;
    }

    public Announcement() {
    }
}
