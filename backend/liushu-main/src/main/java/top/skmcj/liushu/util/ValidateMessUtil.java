package top.skmcj.liushu.util;

import java.util.regex.Pattern;

/**
 * 用于验证常见信息
 */
public class ValidateMessUtil {
    //
    private static Pattern emailPt;

    private static Pattern phonePt;

    private static Pattern linkPt;

    static {
        //静态代码块会随着类的加载而自动执行，且只执行一次
        // 邮箱的正则表达式
        String emailStr = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$" ;
        // 手机的正则表达式
        // /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[189]))\d{8}$/
        String phoneStr = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|" +
                "(?:8[\\d])|(?:9[189]))\\d{8}$";
        String linkStr = "^https?://.+";

        emailPt = Pattern.compile(emailStr);
        phonePt = Pattern.compile(phoneStr);
        linkPt = Pattern.compile(linkStr);
    }

    /**
     * 匹配邮箱号
     * @param email
     * @return
     */
    public static boolean validateEmail(String email) {
        return emailPt.matcher(email).matches();
    }

    /**
     * 匹配手机号
     * @param phone
     * @return
     */
    public static boolean validatePhone(String phone) {
        return phonePt.matcher(phone).matches();
    }

    /**
     * 匹配http或https链接
     * @param link
     * @return
     */
    public static boolean validateLink(String link) {
        return linkPt.matcher(link).matches();
    }

}
