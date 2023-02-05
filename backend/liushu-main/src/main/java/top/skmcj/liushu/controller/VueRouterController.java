package top.skmcj.liushu.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.annotation.Unprefix;
import top.skmcj.liushu.common.enums.FileContentTypeEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Unprefix
@RestController
public class VueRouterController {
    /**
     * 处理商家后台页面的跳转请求
     * 以 /store 开头的路径
     */
    @GetMapping("/store/**")
    public void gotoStoreIndex(HttpServletRequest request, HttpServletResponse response) {
        try {
            /**
             * 如果请求路径对应的静态文件存在，直接返回请求的文件
             * 否则，返回首页文件
             */
            // 输入流
            FileInputStream fileInputStream = null;
            ClassPathResource reqPathResource = new ClassPathResource(request.getRequestURI());
            String type = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(".") + 1);
            this.addContentTypeOfResponse(response, type);
            if(reqPathResource.exists()) {
                // 路径存在，直接返回
                if(reqPathResource.getFile().isFile()) {
                    // 是文件，直接返回
                    fileInputStream = new FileInputStream(reqPathResource.getFile());
                } else {
                    // 不是文件，返回首页
                    ClassPathResource pathResource = new ClassPathResource("/store/index.html");
                    fileInputStream = new FileInputStream(pathResource.getFile());
                }
            } else {
                // 文件不存在，返回首页
                ClassPathResource pathResource = new ClassPathResource("/store/index.html");
                fileInputStream = new FileInputStream(pathResource.getFile());
            }
            // 输出流，将文件响应给浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理用户模块页面的跳转请求
     * 以 / 开头的路径
     */
    @GetMapping("/**")
    public void gotoUserIndex(HttpServletRequest request, HttpServletResponse response) {
        try {
            /**
             * 如果请求路径对应的静态文件存在，直接返回请求的文件
             * 否则，返回首页文件
             */
            // 输入流
            FileInputStream fileInputStream = null;
            ClassPathResource reqPathResource = new ClassPathResource(request.getRequestURI());
            String type = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(".") + 1);
            // System.out.println("request uri ==> " + type);
            this.addContentTypeOfResponse(response, type);
            if(reqPathResource.exists()) {
                // 路径存在，直接返回
                if(reqPathResource.getFile().isFile()) {
                    // 是文件，直接返回
                    fileInputStream = new FileInputStream(reqPathResource.getFile());
                } else {
                    // 不是文件，返回首页
                    ClassPathResource pathResource = new ClassPathResource("/front/index.html");
                    fileInputStream = new FileInputStream(pathResource.getFile());
                }
            } else {
                // 文件不存在，返回首页
                ClassPathResource pathResource = new ClassPathResource("/front/index.html");
                fileInputStream = new FileInputStream(pathResource.getFile());
            }
            // 输出流，将文件响应给浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件类型动态添加 Content-Type
     * @param response
     */
    private void addContentTypeOfResponse(HttpServletResponse response, String fileType) {
        String contentType = FileContentTypeEnum.getTypeOfContentType(fileType);
        if(contentType == null) return;
        response.setContentType(contentType);
    }
}
