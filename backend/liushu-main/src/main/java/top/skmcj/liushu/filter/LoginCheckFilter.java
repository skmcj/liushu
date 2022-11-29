package top.skmcj.liushu.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.util.JwtUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 拦截器，默认拦截所有 /api 接口
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/api/**")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        // 定义不需拦截的请求
        String[] urls = new String[]{
                "/api/business/login",
                "/api/business/logon",
                "/api/business/logout",
                "/api/business/examine",
                // "/api/user/**",
                "/api/mail/**",
                "/api/img/**",
        };
        // 2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        // 3、如果不需要处理，则直接放行
        if(check) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setContentType("application/json;charset=utf-8");
        String token = request.getHeader("Authorization");
        // 4-1、判断商家登录状态，如果已登录，则直接放行
        try {
            log.info("filter url => " + requestURI);
            if(JwtUtil.verifyTokenOfEmployee(token) != null) {
                // 放行
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.getWriter().write(JSON.toJSONString(Result.error(StatusCodeEnum.JWT_ERR), SerializerFeature.WriteMapNullValue));
        }

        // 4-2、判断前端登录状态


        // 5、如果未登录则返回未登录结果，通过输出流的形式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(Result.error(StatusCodeEnum.NO_LOGIN), SerializerFeature.WriteMapNullValue));
        return;
    }

    /**
     * 路径匹配，判断路径是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for(String url: urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
