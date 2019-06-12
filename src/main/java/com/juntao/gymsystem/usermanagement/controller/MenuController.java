package com.juntao.gymsystem.usermanagement.controller;

import com.juntao.gymsystem.usermanagement.domain.Menu;
import com.juntao.gymsystem.usermanagement.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/searchMenu")
    public String searchMenu(Menu menu, RedirectAttributes redirectAttributes) {
        List<Menu> result = menuService.getMenuByName(menu);
        if (result.size()==0) {
            redirectAttributes.addFlashAttribute("searchError","此功能不存在");
            return "redirect:/";
        }
        Menu best = result.get(0);
        for (int j=1;j<result.size();j++) {
            if (best.getName().length()>result.get(j).getName().length()) {
                best = result.get(j);
            }
        }
        if (best.getAddress()==null || best.getAddress().equals("")) {
            redirectAttributes.addFlashAttribute("searchError","此功能不存在");
            return "redirect:/";
        }else {
            return "redirect:"+best.getAddress();
        }
    }
}
