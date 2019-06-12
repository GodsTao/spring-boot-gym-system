package com.juntao.gymsystem.usermanagement.service;

import com.juntao.gymsystem.usermanagement.domain.Menu;
import com.juntao.gymsystem.usermanagement.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public List<Menu> getMenuByName(Menu menu) {
        String name = "%"+menu.getName()+"%";
        return menuMapper.findMenuByName(name);
    }
}
