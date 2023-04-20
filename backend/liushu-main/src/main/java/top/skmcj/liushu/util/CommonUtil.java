package top.skmcj.liushu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * 通用工具
 */
@Component
public class CommonUtil {

    private static String serverName;

    @Value("${liushu.server-name}")
    public void setServerName(String serverName) {
        CommonUtil.serverName = serverName;
    }

    /**
     * 获取当前服务器域名
     * @return
     */
    public static String getDoMain(HttpServletRequest request) {
        if(serverName != null && ValidateMessUtil.validateLink(serverName)) return serverName;
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 获取图片域名链接
     * @param request
     * @return
     */
    public static String getImgDoMain(HttpServletRequest request) {
        if(serverName != null && ValidateMessUtil.validateLink(serverName)) return serverName + "/api/img/";
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/img/";
    }

    /**
     * 获取token信息
     * @param request
     * @return
     */
    public static User getTokenMessByUser(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        return user;
    }

    /**
     * 解码base64编码的URL
     * @param input
     * @return
     */
    public static byte[] base64EncodeUrl(byte[] input) {
        byte[] base64 = Base64.getEncoder().encode(input);
        for (int i = 0; i < base64.length; ++i)
            switch (base64[i]) {
                case '+':
                    base64[i] = '*';
                    break;
                case '/':
                    base64[i] = '-';
                    break;
                case '=':
                    base64[i] = '_';
                    break;
                default:
                    break;
            }
        return base64;
    }
}
