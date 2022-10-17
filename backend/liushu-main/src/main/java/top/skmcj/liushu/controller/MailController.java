package top.skmcj.liushu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.util.MailServerUtil;
import top.skmcj.liushu.util.ValidateCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailServerUtil mailServerUtil;

    @GetMapping("/code")
    public Result<String> sendValidateCode(@RequestParam String type, @RequestParam String to, HttpServletRequest request) {
        System.out.println("type => " + type);
        System.out.println("to => " + to);
        String cause = this.getMailCause(type);
        String code = ValidateCodeUtil.getValidateCodeNum(6);
        HttpSession session = request.getSession();
        session.setAttribute("code", code);
        Map<String, String> data = new HashMap<>();
        data.put("cause", cause);
        data.put("vCode", code);
        data.put("validTime", "5");
        try {
            mailServerUtil.sendVCTemplateMail(to, "验证码", data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("邮件发送失败");
        }
        return Result.success("验证码发送成功");
    }

    /**
     * 获取发件原因
     * @param type
     * @return
     */
    private String getMailCause(String type) {
        /**
         * slo - shop logon => 商家注册
         */
        String cause;
        switch (type) {
            case "slo":
                cause = "您正在注册成为流书网-图书外卖平台的入驻商家";
                break;
            default:
                cause = "您正在请求的业务需要验证码";
        }
        return cause;
    }
}
