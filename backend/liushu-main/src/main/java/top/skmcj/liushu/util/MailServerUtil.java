package top.skmcj.liushu.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class MailServerUtil {

    @Autowired
    private Configuration configuration;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.nickname}")
    private String nickname;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${liushu.mail.mode}")
    private String mailMode;

    /**
     * 发送简单邮件
     * @param to 收件人邮箱
     * @param subject 发送主题
     * @param text 发送内容文本
     */
    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 发件人
        message.setFrom(nickname + '<' + from + '>');
        // 收件人
        message.setTo(to);
        // 邮件主题
        message.setSubject(subject);
        // 邮件内容
        message.setText(text);
        // 发送邮件
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param content HTML文本
     * @throws MessagingException
     */
    public void sendHTMLMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        /* 设置邮件重要级别 */
        message.setHeader("Importance", "High");
        // 设置相关参数
        messageHelper.setFrom(nickname + '<' + from + '>');
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        // 发送邮件
        mailSender.send(message);
    }

    /**
     * 发送验证码模板邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param data 模板替换数据
     *             - cause 发件原因
     *             - vCode 验证码
     *             - validTime 验证码有效时间
     */
    public void sendVCTemplateMail(String to, String subject, Map data) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        // 导入模板，获取邮件内容
        Template template = configuration.getTemplate("vcmail.ftl");
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        /* 设置邮件重要级别 */
        message.setHeader("Importance", "High");
        // 设置相关参数
        messageHelper.setFrom(nickname + '<' + from + '>');
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        // 发送邮件
        mailSender.send(message);
    }

    /**
     * 发送通知模板邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param data 模板替换数据
     *             - cause 发件原因
     *             - vCode 验证码
     *             - validTime 验证码有效时间
     */
    public void sendExTemplateMail(String to, String subject, Map data) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        // 导入模板，获取邮件内容
        Template template = configuration.getTemplate("exmail.ftl");
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        /* 设置邮件重要级别 */
        message.setHeader("Importance", "High");
        // 设置相关参数
        messageHelper.setFrom(nickname + '<' + from + '>');
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        // 发送邮件
        mailSender.send(message);
    }

    /**
     * 发送通知模板邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param data 模板替换数据
     *             - cause 发件原因
     *             - vCode 验证码
     *             - validTime 验证码有效时间
     */
    public void sendPrTemplateMail(String to, String subject, Map data) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        // 导入模板，获取邮件内容
        Template template = configuration.getTemplate("pemail.ftl");
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        /* 设置邮件重要级别 */
        message.setHeader("Importance", "High");
        // 设置相关参数
        messageHelper.setFrom(nickname + '<' + from + '>');
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        // 发送邮件
        mailSender.send(message);
    }

    /**
     * 处理邮件模式
     * @param to
     * @param data
     * @param type [code | examine | link]
     * @return
     */
    public boolean handleMailMode(String to, Map<String, String> data, String type) {
        /**
         * code - 验证码
         * examine - 商家审核
         * link - 友情链接审核
         */
        if("console".equals(mailMode)) {
            switch (type) {
                case "code":
                    log.info("邮件信息(验证码)······\n==>\t目标邮箱: {}\n==>\t提示信息: {}\n==>\t验 证 码: {}\n==>\t有效时间: " +
                              "{}\n==>\t创建时间: {}",
                            to, data.get("cause"), data.get("vCode"), data.get("validTime") + " 分钟",
                            LocalDateTime.now());
                    break;
                case "examine":
                    log.info("邮件信息(商家审核通知)······\n==>\t目标邮箱: {}\n==>\t提示信息: {}\n==>\t审核结果: {}\n==>\t创建时间: {}",
                            to, data.get("cause"), data.get("result"), LocalDateTime.now());
                    break;
                case "link":
                    log.info("邮件信息(友情链接审核通知)······\n==>\t目标邮箱: {}\n==>\t提示信息: {}\n==>\t审核结果: {}\n==>\t创建时间: {}",
                            to, data.get("cause"), data.get("result"), LocalDateTime.now());
                    break;
            }
        } else if("send".equals(mailMode)) {
            try {
                // 发送邮件
                switch (type) {
                    case "code":
                        this.sendVCTemplateMail(to, "验证码", data);
                        break;
                    case "examine":
                        this.sendExTemplateMail(to, "审核状态", data);
                        break;
                    case "link":
                        this.sendPrTemplateMail(to, "审核状态", data);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {

        }
        return true;
    }

}
