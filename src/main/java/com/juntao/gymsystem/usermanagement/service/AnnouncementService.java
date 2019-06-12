package com.juntao.gymsystem.usermanagement.service;

import com.juntao.gymsystem.usermanagement.domain.Announcement;

public interface AnnouncementService {
    /**
     * 发布公告
     * @param announcement
     */
    public void saveAnnouncement(Announcement announcement);

    /**
     * 修改公告
     * @param announcement
     */
    public void updateAnnouncement(Announcement announcement);

    /**
     * 查找是否有公告
     * @return
     */
    public Announcement findAnnouncement(Long userId);

    /**
     * 查找当前公告
     * @return
     */
    public Announcement getAnnouncement();
}
