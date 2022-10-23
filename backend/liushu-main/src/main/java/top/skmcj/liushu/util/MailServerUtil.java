package top.skmcj.liushu.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

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
}
