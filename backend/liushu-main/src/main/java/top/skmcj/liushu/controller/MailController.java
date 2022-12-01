package top.skmcj.liushu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.entity.Link;
import top.skmcj.liushu.service.BookstoreService;
import top.skmcj.liushu.service.LinkService;
import top.skmcj.liushu.util.MailServerUtil;
import top.skmcj.liushu.util.ValidateCodeUtil;
import top.skmcj.liushu.vo.LinkVo;

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
    private LinkService linkService;

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
        System.out.println("生成的验证码 => " + code + ", 有效时间 => " + String.valueOf(mailTime / 60) + "分钟, 验证码类型 =>" + type);
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
    public Result<String> sendExamineNotice(@RequestParam Long storeId) {
        // 一些身份验证
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
     * 发送友情链接审核状态通知邮件
     * @param linkId
     * @return
     */
    @GetMapping("/sendLinkEN")
    public Result<String> sendLinkProcessNotice(@RequestParam Long linkId) {
        if(linkId == null) {
            return Result.error("链接ID不能为空");
        }
        Link link = linkService.getById(linkId);
        if(link == null) return Result.error("找不到相应的链接");
        Map<String, String> data = new HashMap<>();
        data.put("cause", "您之前所提交的友情链接审核结果已出");
        data.put("result", link.getFlag() == 1 ? "通过" : "不通过");
        try {
            // 发送邮件
            mailServerUtil.sendPrTemplateMail(link.getEmail(), "审核状态", data);
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
         * ulo - user logon => 用户注册
         * uce - user change email => 用户修改邮箱
         * une - user new email => 用户新邮箱
         * ucp - user change password => 用户修改密码
         * usp - user set payPassword => 用户设置支付密码
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
            case "ulo":
                cause = "感谢你注册流书网";
                break;
            case "uce":
                cause = "您正在修改流书网绑定邮箱，需验证当前绑定邮箱";
                break;
            case "une":
                cause = "您正在修改流书网绑定邮箱，需验证新邮箱是否可用";
                break;
            case "ucp":
                cause = "您正在修改流书网密码，需验证身份";
                break;
            case "usp":
                cause = "您正在设置流书网支付密码";
                break;
            default:
                cause = "您正在请求的业务需要验证码";
        }
        return cause;
    }
}
