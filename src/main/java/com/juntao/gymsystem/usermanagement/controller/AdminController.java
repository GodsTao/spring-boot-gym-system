package com.juntao.gymsystem.usermanagement.controller;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.juntao.gymsystem.usermanagement.domain.Announcement;
import com.juntao.gymsystem.usermanagement.domain.Response;
import com.juntao.gymsystem.usermanagement.domain.User;
import com.juntao.gymsystem.usermanagement.service.AnnouncementService;
import com.juntao.gymsystem.usermanagement.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 获取用户管理页
     * @return
     */
    @GetMapping("/list")
    public ModelAndView listUser(Model model) {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userModel",userService.getUserByUsername(currentUser.getUsername()));
       return new ModelAndView( "/admin/index","user",model);
    }

    /**
     * 分页的异步操作
     * @param draw
     * @param start
     * @param length
     * @param username
     * @return
     */
    @GetMapping("/page")
    public Map<String,Object> userPage(@RequestParam(value="draw",defaultValue = "0") int draw,
                                       @RequestParam(value = "start",defaultValue = "0")int start,
                                       @RequestParam(value = "length",defaultValue = "10") int length,
                                       @RequestParam(value = "username",required =false,defaultValue ="")String username) {
        Map<String,Object> result = new HashMap<>();
        Page<User> userList =userService.getAllByUsernameLike(start/length+1,length,username);
        //需要把page对象包装成pageInfo才能序列化
        PageInfo<User> userPageInfo = new PageInfo<>(userList);
        List<User> users=userPageInfo.getList();

        result.put("draw",draw);
        result.put("recordsTotal",userPageInfo.getTotal());
        result.put("recordsFiltered",userPageInfo.getTotal());
        result.put("data",users);
        result.put("error","");

        return result;
    }

    /**
     * 新增用户
     * @param user
     * @param roleId
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Response> saveUser(User user,Long roleId) {

        try {
            userService.saveUser(user,roleId);
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,"服务器异常",null));
        }
        return ResponseEntity.ok().body(new Response(true,"新增成功",null));
    }

    /**
     * 编辑用户信息
     * @param user
     * @param roleId
     * @return
     */
    @PostMapping("/edit")
    public ResponseEntity<Response> updateUser(User user,Long roleId) {
        try{
            userService.updateUser(user,roleId);
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,"服务器异常",null));
        }
        return ResponseEntity.ok().body(new Response(true,"修改成功",null));
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("id") Long userId) {
        try{
            userService.deleteUser(userId);
        }catch(Exception e) {
            return ResponseEntity.ok().body(new Response(false,"服务器异常"));
        }
        return ResponseEntity.ok().body(new Response(true,"删除成功"));
    }

    /**
     * 获取用户编辑界面
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public ModelAndView getEditForm(@PathVariable("id")Long userId,Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("userModel",user);
        return new ModelAndView("/admin/edit","user",model);
    }

    /**
     * 获取新增用户界面
     * @param model
     * @return
     */
    @GetMapping("/getAdd")
    public ModelAndView getAddForm(Model model) {
        User  currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userModel",userService.getUserByUsername(currentUser.getUsername()));
        return new ModelAndView("admin/add","user",model);
    }


    @GetMapping("/getAnnouncement")
    public ModelAndView getAnnouncementForm(Model model) {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userModel",userService.getUserByUsername(currentUser.getUsername()));
        return new ModelAndView("/admin/announcement","user",model);
    }

    @PostMapping("/updateAnnouncement")
    public ModelAndView updateAnnouncement(Announcement announcement) {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        announcement.setUserId(currentUser.getId());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd : HH:mm");
        String createTime =simpleDateFormat.format(date);
        announcement.setCreateTime(createTime);
        if (announcementService.getAnnouncement()==null) {
            announcementService.saveAnnouncement(announcement);
        }else{
            announcementService.updateAnnouncement(announcement);
        }
        return new ModelAndView("redirect:/");
    }
}
