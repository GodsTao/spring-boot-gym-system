package com.juntao.gymsystem.usermanagement.controller;
import com.juntao.gymsystem.usermanagement.domain.Response;
import com.juntao.gymsystem.usermanagement.domain.User;
import com.juntao.gymsystem.usermanagement.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/u")
public class UserSpaceController {
    @Autowired
    private UserService userService;

    private final ResourceLoader resourceLoader;

    @Value("${web.upload-path}")
    private String imagePath;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.address}")
    private String serverAddress;

    @Autowired
    public UserSpaceController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 获取个人中心页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String getUserSpace(@PathVariable(value = "username",required = false) String username, Model model) {
        User user =userService.getUserByUsername(username);
        model.addAttribute("userModel",user);
        return "user/userspace";
    }

    /**
     * 获取完善个人信息页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    public String getProfileForm(@PathVariable(value = "username")String username,Model model) {
        User user =userService.getUserByUsername(username);
        model.addAttribute("userModel",user);
        return "user/profile";
    }

    /**
     * 保存个人信息
     * @param username
     * @param user
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping("/{username}/saveProfile")
    public String saveProfile(@PathVariable(value = "username")String username, User user, RedirectAttributes redirectAttributes,Model model) {
        User  currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (currentUser.getUsername().equals(username)){
            User result =userService.getUserByUsername(username);

            result.setName(user.getName());
            result.setBirthday(user.getBirthday());
            result.setClasses(user.getClasses());
            result.setCollege(user.getCollege());
            result.setMajor(user.getMajor());
            result.setSignature(user.getSignature());
            result.setPhoneNumber(user.getPhoneNumber());
            userService.updateProfile(result);
            redirectAttributes.addFlashAttribute("successMsg","个人信息完善成功");
            return "redirect:/u/"+username;
        }else{
            model.addAttribute("errorMsg","当前用户不能进行此操作");
            model.addAttribute("userModel",currentUser);
            return "user/profile";
        }
    }

    /**
     * 获取密码修改页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/getPasswordForm")
    public String getPasswordForm(@PathVariable(value = "username") String username,Model model) {
        User user = userService.getUserByUsername(username);
        model.addAttribute("userModel",user);
        return "user/editPassword";
    }

    /**
     * 修改密码
     * @param username
     * @param oldPassword
     * @param newPassword
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping("/{username}/editPassword")
    public String editPassword(@PathVariable(value = "username")String username,String oldPassword,String newPassword,RedirectAttributes redirectAttributes,Model model){
        User  currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (currentUser.getUsername().equals(username)){
            User user = userService.getUserByUsername(username);
            if (user!=null) {
                if(BCrypt.checkpw(oldPassword, user.getPassword())) {
                    user.setPassword(newPassword);
                    userService.updateForgetedPassword(user);
                    Subject current = SecurityUtils.getSubject();
                    current.logout();
                    redirectAttributes.addFlashAttribute("successMsg","修改成功,请重新登录");
                    return "redirect:/toLogin";
                }else {
                    model.addAttribute("errorMsg","原密码错误" );
                    model.addAttribute("userModel",user);
                    return "user/editPassword";
                }
            }else{
                model.addAttribute("errorMsg","此用户不存在" );
                model.addAttribute("userModel",currentUser);
                return "user/editPassword";
            }
        }else {
            model.addAttribute("errorMsg","当前用户不能进行此操作");
            model.addAttribute("userModel",currentUser);
            return "user/editPassword";
        }

    }

    /**
     * 获取上传头像页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    public ModelAndView getAvatar(@PathVariable(value = "username")String username,Model model) {
        User user = userService.getUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("user/avatar","userModel",model);
    }

    /**
     * 上传头像图片到本地（更好的做法是自己制作文件服务器，上传到文件服务器数据）
     * @param username
     * @param file
     * @return
     */
    @PostMapping("/{username}/upload")
    public ResponseEntity<Response> saveAvatar(@PathVariable(value = "username")String username, @RequestParam("file") MultipartFile file) {
        User user = userService.getUserByUsername(username);
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();

        String destFileName = imagePath + File.separator + fileName;

        //第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录
        File destFile = new File(destFileName);
        destFile.getParentFile().mkdirs();
        //把浏览器上传的文件复制到希望的位置
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false,"出现异常",null));
        }
        //把上一个头像图片给删除
        String lastFileName = imagePath + File.separator + StringUtils.substringAfterLast(user.getAvatar(),"/");
        File lastFile = new File(lastFileName);
        if (!lastFile.isDirectory()) {
            lastFile.delete();
        }

        String path ="//"+serverAddress +":"+serverPort+ "/u//show/"+fileName;

        userService.saveAvatar(username,path);
        return ResponseEntity.ok().body(new Response(true,"处理成功",path));
    }

    @GetMapping("/show/{fileName}")
    public ResponseEntity showPicture(@PathVariable("fileName") String fileName) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + imagePath +'/'+fileName));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
