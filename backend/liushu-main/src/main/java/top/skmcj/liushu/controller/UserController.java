package top.skmcj.liushu.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.UserDto;
import top.skmcj.liushu.entity.User;
import top.skmcj.liushu.entity.UserInfo;
import top.skmcj.liushu.service.UserInfoService;
import top.skmcj.liushu.service.UserService;
import top.skmcj.liushu.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MailServerUtil mailServerUtil;

    @Value("${liushu.captcha.time}")
    private int mailTime;

    /**
     * 用户登录
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result<UserDto> login(@RequestBody User user, HttpServletRequest request) {
        User loginUser = null;
        UserDto userDto = new UserDto();
        String username = user.getUsername();
        // 判断username是用户名还是邮箱
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if(ValidateMessUtil.validateEmail(username)) {
            // 邮箱
            queryWrapper.eq(User::getEmail, username);
            loginUser = userService.getOne(queryWrapper);
        } else {
            // 用户名
            queryWrapper.eq(User::getUsername, username);
            loginUser = userService.getOne(queryWrapper);
        }
        if(loginUser == null) {
            return Result.error(StatusCodeEnum.USERNAME_NOT_EXIST);
        }
        // 用户存在，比对密码
        if(loginUser.getStatus() != 1) {
            // 用户被禁用
            return Result.error(StatusCodeEnum.USER_STATUS_ERR);
        }
        // 用户状态正常，比对密码
        if(loginUser.getPassword().equals(user.getPassword())) {
            // 密码正确，登录
            // 获取token
            String token = JwtUtil.getToken(loginUser);
            // 获取用户详细信息
            LambdaQueryWrapper<UserInfo> infoQueryWrapper = new LambdaQueryWrapper<>();
            infoQueryWrapper.eq(UserInfo::getUserId, loginUser.getId());
            UserInfo userInfo = userInfoService.getOne(infoQueryWrapper);
            if (userInfo.getAvatar() != null) {
                String url = CommonUtil.getImgDoMain(request) + userInfo.getAvatar();
                userDto.setAvatar(userInfo.getAvatar());
                userDto.setAvatarUrl(url);
            }
            userDto.setId(loginUser.getId());
            userDto.setUsername(loginUser.getUsername());
            userDto.setEmail(loginUser.getEmail());
            userDto.setToken(token);
            userDto.setNickname(userInfo.getNickname());
            userDto.setBirthday(userInfo.getBirthday());
            userDto.setMoney(userInfo.getMoney());
            userDto.setSignature(userInfo.getSignature());
            userDto.setSex(userInfo.getSex());
            return Result.success(userDto, StatusCodeEnum.USER_LOGIN_OK);
        } else {
            // 密码错误
            return Result.error(StatusCodeEnum.USER_PASS_ERR);
        }
    }

    /**
     * 用户注册
     * @param request
     * @return
     */
    @PostMapping("/logon")
    public Result<String> logon(@RequestBody UserDto userDto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String realCode = (String) session.getAttribute("code");
        Long codeTime = (Long) session.getAttribute("codeTime");
        long nowTime = System.currentTimeMillis();
        // 校验验证码
        if(realCode == null) {
            return Result.error("请先获取验证码");
        }
        if(realCode.equals(userDto.getCode()) && nowTime <= codeTime) {
            // 校验通过，继续
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            // 获取用户名
            queryWrapper.eq(User::getUsername, userDto.getUsername());
            User oldUser = userService.getOne(queryWrapper);
            // 判断用户名是否可用
            if(oldUser != null) {
                // 不可用，返回信息 => 用户名已存在
                return Result.error(StatusCodeEnum.USERNAME_EXIST);
            } else {
                // 可用继续
                User user = new User();
                user.setUsername(userDto.getUsername());
                user.setEmail(userDto.getEmail());
                user.setPassword(userDto.getPassword());
                // 添加新用户
                userService.save(user);
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(user.getId());
                // 设置昵称默认值为用户名
                userInfo.setNickname(userDto.getUsername());
                // 添加用户详情记录
                userInfoService.save(userInfo);
                return Result.success(StatusCodeEnum.USER_LOGON_OK);
            }
        } else if(nowTime > codeTime) {
            // 过期
            return Result.error(StatusCodeEnum.CHECK_CODE_EXPIRED);
        } else {
            return Result.error(StatusCodeEnum.CHECK_CODE_ERR);
        }
    }

    /**
     * 设置用户信息
     * @param userInfo
     * @return
     */
    @PutMapping("/info")
    public Result<String> updateUserInfo(@RequestBody UserInfo userInfo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userInfo.getUserId());
        UserInfo targetUser = new UserInfo();
        targetUser.setAvatar(userInfo.getAvatar());
        targetUser.setNickname(userInfo.getNickname());
        targetUser.setSex(userInfo.getSex());
        targetUser.setSignature(userInfo.getSignature());
        targetUser.setBirthday(userInfo.getBirthday());
        boolean flag = userInfoService.update(targetUser, queryWrapper);
        if(flag) {
            return Result.success("用户信息修改成功");
        } else {
            return Result.error("用户信息修改失败");
        }
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/info")
    public Result<UserDto> getUserInfo(@RequestParam String id, HttpServletRequest request) {
        UserDto userDto = new UserDto();
        User user = userService.getById(id);
        if(user == null) {
            return Result.error("用户获取失败");
        }
        LambdaQueryWrapper<UserInfo> infoQueryWrapper = new LambdaQueryWrapper<>();
        infoQueryWrapper.eq(UserInfo::getUserId, user.getId());
        UserInfo userInfo = userInfoService.getOne(infoQueryWrapper);
        if (userInfo.getAvatar() != null) {
            String url = CommonUtil.getImgDoMain(request) + userInfo.getAvatar();
            userDto.setAvatar(userInfo.getAvatar());
            userDto.setAvatarUrl(url);
        }
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setNickname(userInfo.getNickname());
        userDto.setBirthday(userInfo.getBirthday());
        userDto.setMoney(userInfo.getMoney());
        userDto.setSignature(userInfo.getSignature());
        userDto.setSex(userInfo.getSex());
        return Result.success(userDto, StatusCodeEnum.GET_OK);
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @PutMapping("/pass")
    public Result<String> updateUserPassword(@RequestBody User user) {
        if(user.getPassword() == null || user.getPassword().length() == 0) {
            return Result.error("新密码不能为空");
        }
        User oldUser = userService.getById(user.getId());
        if(oldUser == null) {
            return Result.error("用户信息有误");
        }
        if(!oldUser.getEmail().equals(user.getEmail())) {
            return Result.error("用户信息有误");
        }
        if(!oldUser.getUsername().equals(user.getUsername())) {
            return Result.error("用户信息有误");
        }
        // 用户信息校验正常，修改密码
        User newUser = new User();
        newUser.setId(oldUser.getId());
        newUser.setPassword(user.getPassword());
        boolean flag = userService.updateById(newUser);
        if(!flag) return Result.error("系统繁忙，请稍后再试");
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

    /**
     * 修改邮箱
     * @return
     */
    @PutMapping("/email")
    public Result<String> updateUserEmail(@RequestBody User user) {
        if(user.getEmail() == null || user.getEmail().length() == 0) {
            return Result.error("用户邮箱不可为空");
        }
        User oldUser = userService.getById(user.getId());
        if(oldUser == null) {
            return Result.error("用户信息有误");
        }
        if(!oldUser.getUsername().equals(user.getUsername())) {
            return Result.error("用户信息有误");
        }
        // 用户信息校验正常，修改邮箱
        User newUser = new User();
        newUser.setId(oldUser.getId());
        newUser.setEmail(user.getEmail());
        boolean flag = userService.updateById(newUser);
        if(!flag) return Result.error("系统繁忙，请稍后再试");
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

    /**
     * 获取注册所需验证码
     * @param to
     * @param request
     * @return
     */
    @GetMapping("/logon/code")
    public Result<String> validateCode(@RequestParam String to, HttpServletRequest request) {
        // 验证邮箱是否已注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, to);
        User oldUser = userService.getOne(queryWrapper);
        if(oldUser != null) {
            // 邮箱已注册
            return Result.error(StatusCodeEnum.USER_EMAIL_EXIST);
        }
        // 未注册，继续发送验证码
        String cause = "感谢您注册流书网";
        String code = ValidateCodeUtil.getValidateCodeNum(6);
        HttpSession session = request.getSession();
        session.setAttribute("code", code);
        session.setAttribute("codeTime", System.currentTimeMillis() + mailTime * 1000);
        System.out.println(to + " 用户注册：验证码 => " + code + ", 有效时间 => " + String.valueOf(mailTime / 60) + "分钟");
        Map<String, String> data = new HashMap<>();
        data.put("cause", cause);
        data.put("vCode", code);
        data.put("validTime", String.valueOf(mailTime / 60));
        try {
            // 发送邮件
            // mailServerUtil.sendVCTemplateMail(to, "验证码", data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(StatusCodeEnum.MAIL_SEND_ERR);
        }
        return Result.success(StatusCodeEnum.MAIL_SEND_OK);
    }

    /**
     * 校验用户token
     * @param token
     * @return
     */
    @GetMapping("/validate/token")
    public Result<Map> validateToken(@RequestParam String token) throws Exception {
        // System.out.println(token);
        DecodedJWT verify = JwtUtil.verifyToken(token);
        if(!verify.getClaim("type").asString().equals("user")) {
            throw new JWTDecodeException("无法解析为用户对象");
        }
        Map<String, String> data = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        data.put("expiresAt", sdf.format(verify.getExpiresAt()));
        return Result.success(data, StatusCodeEnum.JWT_OK);
    }

}
