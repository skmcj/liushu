package top.skmcj.liushu.util;

import top.skmcj.liushu.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用工具
 */
public class CommonUtil {
    /**
     * 获取当前服务器域名
     * @return
     */
    public static String getDoMain(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 获取图片域名链接
     * @param request
     * @return
     */
    public static String getImgDoMain(HttpServletRequest request) {
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
}
