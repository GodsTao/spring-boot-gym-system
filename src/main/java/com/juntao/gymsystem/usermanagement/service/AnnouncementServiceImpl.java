package com.juntao.gymsystem.usermanagement.service;

import com.juntao.gymsystem.usermanagement.domain.Announcement;
import com.juntao.gymsystem.usermanagement.mapper.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl implements AnnouncementService{

    @Autowired
    AnnouncementMapper announcementMapper;

    @Override
    public void saveAnnouncement(Announcement announcement) {
        announcementMapper.saveAnnouncement(announcement);
    }

    @Override
    public void updateAnnouncement(Announcement announcement) {
        announcementMapper.updateAnnouncement(announcement);
    }

    @Override
    public Announcement findAnnouncement(Long userId) {
        return announcementMapper.findAnnouncement(userId);
    }

    @Override
    public Announcement getAnnouncement() {
        return announcementMapper.findAll();
    }
}
