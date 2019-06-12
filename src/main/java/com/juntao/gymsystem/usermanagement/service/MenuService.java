package com.juntao.gymsystem.usermanagement.service;

import com.juntao.gymsystem.usermanagement.domain.Menu;
import com.juntao.gymsystem.usermanagement.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MenuService {

    /**
     * 通过关键词获取功能
     * @param menu
     * @return
     */
    List<Menu> getMenuByName(Menu menu);
}
