package com.juntao.gymsystem.usermanagement.controller;

import com.juntao.gymsystem.usermanagement.domain.Announcement;
import com.juntao.gymsystem.usermanagement.domain.Response;
import com.juntao.gymsystem.usermanagement.domain.User;
import com.juntao.gymsystem.usermanagement.service.AnnouncementService;
import com.juntao.gymsystem.usermanagement.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/")
    public String root(Model model){
        Announcement announcement =announcementService.getAnnouncement();
        model.addAttribute("announcement",announcement);

        User  currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (currentUser!=null) {
            User newUser = userService.getUserByUsername(currentUser.getUsername());
            model.addAttribute("userModel",newUser);
        }else {
            model.addAttribute("userModel",new User(null,null,null));
        }
        return "index";
    }

    @GetMapping("/index")
    public String toIndex(Model model) {
        Announcement announcement =announcementService.getAnnouncement();
        model.addAttribute("announcement",announcement);

        User  currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if(currentUser!=null) {
            User newUser = userService.getUserByUsername(currentUser.getUsername());
            model.addAttribute("userModel", newUser);
        }else {
            model.addAttribute("userModel",new User(null,null,null));
        }
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(String username, String password, boolean rememberMe, RedirectAttributes redirectAttributes) {
        /**
         *使用shiro编写认证操作
         */
        //1、获取subject
        Subject subject = SecurityUtils.getSubject();
        //2、封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username,password,rememberMe);
        //3、执行登录方法
        try {
            subject.login(token);
        }catch (UnknownAccountException e){
            return new ModelAndView("redirect:/login-error?error=1" );
        }catch (AuthenticationException e) {
            return new ModelAndView("redirect:/login-error?error=2" );
        }
         User user =userService.getUserByUsername(username);
         redirectAttributes.addFlashAttribute("userModel",user);
         return new ModelAndView("redirect:/index");
    }

    @GetMapping("/login-error")
    public String loginError(@RequestParam(value = "error")String error,Model model){
        if (error.equals("1")) {
            model.addAttribute("uMsg","用户名不存在");
            return "/login-error";
        }else{
            model.addAttribute("pMsg","密码输入错误");
            return "/login-error";
        }
    }

    @GetMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    @GetMapping("/checkUsername")
    public ResponseEntity<Response> checkUsername(@RequestParam(value ="username") String username) {
        User user = userService.getUserByUsername(username);
        if (user!=null){
            return ResponseEntity.ok().body(new Response(false,"该账号已存在",null));
        }else {
            return ResponseEntity.ok().body(new Response(true,"该账号可以注册",null));
        }
    }

    @PostMapping("/register")
    public String register(User user,Model model) {
        if (userService.getUserByUsername(user.getUsername())==null){
            userService.saveUser(user,null);
        }else {
            model.addAttribute("msg","用户名已存在");
            return "/register";
        }
        return "redirect:/toLogin";
    }

    @GetMapping("/getForgetPasswordForm")
    public String getForgetPasswordForm(){
        return "forgetPassword";
    }

    @PostMapping("/forgetPassword")
    public String submitPasswordForm(User user,Model model) {
        User result = userService.getUserByUsername(user.getUsername());
        if (result!=null){
            if (!result.getEmail().equals(user.getEmail())) {
                model.addAttribute("errorMsg", "邮箱填写错误");
                return "forgetPassword";
            }
            result.setPassword(user.getPassword());
            userService.updateForgetedPassword(result);
            return "redirect:/toLogin";
        }else{
            model.addAttribute("errorMsg", "用户名不存在 ");
            return "forgetPassword";
        }
    }
}
