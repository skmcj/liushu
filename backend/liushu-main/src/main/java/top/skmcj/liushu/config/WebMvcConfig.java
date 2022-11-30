package top.skmcj.liushu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.skmcj.liushu.annotation.Unprefix;
import top.skmcj.liushu.common.JacksonObjectMapper;

import java.util.List;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置商家后台访问路径映射资源
        registry.addResourceHandler("/store/**").addResourceLocations("classpath:/store/");
        // 配置用户前台访问路径映射资源
        registry.addResourceHandler("/**").addResourceLocations("classpath:/front/");
        // 配置其它路径映射资源
        registry.addResourceHandler("/other/**").addResourceLocations("classpath:/other/");
    }

    /**
     * 处理跨域请求
     * @param registry
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        // 添加映射路径
        registry.addMapping("/**")
                // 放行哪些原始域
                .allowedOriginPatterns("*")
                // 是否发送Cookie信息
                .allowCredentials(true)
                // 放行哪些原始域(请求方式)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
                // 放行哪些原始域(头部信息)
                .allowedHeaders("*");
                // 暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
                // .exposedHeaders("Header1", "Header2");
                // 预请求的结果有效期，默认1800分钟,3600是一小时
                // .maxAge(3600);
    }

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用jackson将java对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的转换器添加到mvc消息转换器的集合中
        converters.add(0, messageConverter);
    }

    @Override
    protected void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> !c.isAnnotationPresent(Unprefix.class));
    }
}
