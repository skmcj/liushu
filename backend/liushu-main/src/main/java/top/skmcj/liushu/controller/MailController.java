package top.skmcj.liushu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.util.MailServerUtil;
import top.skmcj.liushu.util.ValidateCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件相关接口
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private BookstoreService bookstoreService;

    @Autowired
    private MailServerUtil mailServerUtil;

    @Value("${liushu.captcha.time}")
    private int mailTime;

    /**
     * 发送邮箱验证码
     * @param type 发件原因类型
     * @param to 接收邮箱
     * @param request
     * @return
     */
    @GetMapping("/code")
    public Result<String> sendValidateCode(@RequestParam String type, @RequestParam String to, HttpServletRequest request) {
        String cause = this.getMailCause(type);
        String code = ValidateCodeUtil.getValidateCodeNum(6);
        HttpSession session = request.getSession();
        session.setAttribute("code", code);
        session.setAttribute("codeTime", System.currentTimeMillis() + mailTime * 1000);
        System.out.println("生成的验证码 => " + code + ", 有效时间 => " + String.valueOf(mailTime / 60) + "分钟");
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
     * 发送商家审核状态的通知邮件
     * @return
     */
    @GetMapping("/sendExamine")
    public Result<String> sendExamineNotice(@RequestParam String storeId) {
        Bookstore store = bookstoreService.getById(storeId);
        Map<String, String> data = new HashMap<>();
        data.put("cause", "您之前所提交的书店审核资料结果已出");
        data.put("result", store.getAuditStatus() == 2 ? "1" : "0");
        try {
            // 发送邮件
            mailServerUtil.sendExTemplateMail(store.getEmail(), "审核状态", data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(StatusCodeEnum.MAIL_SEND_ERR);
        }
        return Result.success(StatusCodeEnum.MAIL_SEND_OK);
    }

    /**
     * 校验验证码
     * @param code
     * @return
     */
    @GetMapping("/checkCode")
    public Result<String> checkCode(@RequestParam String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String realCode = (String) session.getAttribute("code");
        Long codeTime = (Long) session.getAttribute("codeTime");
        long nowTime = System.currentTimeMillis();
        if(realCode == null) {
            return Result.error("请先获取验证码");
        }
        if(code.equals(realCode) && nowTime <= codeTime) {
            return Result.success(StatusCodeEnum.CHECK_CODE_OK);
        } else if(nowTime > codeTime) {
            return Result.error(StatusCodeEnum.CHECK_CODE_EXPIRED);
        } else {
            return Result.error(StatusCodeEnum.CHECK_CODE_ERR);
        }
    }

    /**
     * 获取发件原因
     * @param type
     * @return
     */
    private String getMailCause(String type) {
        /**
         * slo - shop logon => 商家注册
         * sce - shop change email => 商家修改邮箱
         * sne - shop new email => 商家验证新邮箱
         */
        String cause;
        switch (type) {
            case "slo":
                cause = "您正在注册成为流书网-图书外卖平台的入驻商家";
                break;
            case "sce":
                cause = "您正在修改商家绑定邮箱，需验证当前绑定邮箱";
                break;
            case "sne":
                cause = "您正在修改商家绑定邮箱，需验证新邮箱是否可用";
                break;
            default:
                cause = "您正在请求的业务需要验证码";
        }
        return cause;
    }
}
