package top.skmcj.liushu.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.annotation.Unprefix;
import top.skmcj.liushu.common.enums.FileContentTypeEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
            InputStream fileInputStream = null;
            ClassPathResource reqPathResource = new ClassPathResource(request.getRequestURI());
            String type = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(".") + 1);
            this.addContentTypeOfResponse(response, type);
            if(reqPathResource.exists()) {
                // 路径存在，直接返回
                if(this.customGetInputStream(reqPathResource) != null) {
                    // 是文件，直接返回
                    if(this.customIsFile(request.getRequestURI())) {
                        fileInputStream = reqPathResource.getInputStream();
                    } else {
                        fileInputStream = this.customGetInputStreamOfIndex("/store/index.html");
                    }

                } else {
                    // 不是文件，返回首页
                    fileInputStream = this.customGetInputStreamOfIndex("/store/index.html");
                }
            } else {
                // 文件不存在，返回首页
                fileInputStream = this.customGetInputStreamOfIndex("/store/index.html");
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
            // FileInputStream fileInputStream = null;
            InputStream fileInputStream = null;
            ClassPathResource reqPathResource = new ClassPathResource(request.getRequestURI());
            String type = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(".") + 1);
            // System.out.println("request uri ==> " + type);
            this.addContentTypeOfResponse(response, type);
            if(reqPathResource.exists()) {
                // 路径存在，直接返回

                if(this.customGetInputStream(reqPathResource) != null) {
                    // 是文件，直接返回
                    if(this.customIsFile(request.getRequestURI())) {
                        fileInputStream = reqPathResource.getInputStream();
                    } else {
                        // 不是文件，返回首页
                        fileInputStream = this.customGetInputStreamOfIndex("/front/index.html");
                    }
                } else {
                    // 不是文件，返回首页
                    fileInputStream = this.customGetInputStreamOfIndex("/front/index.html");
                }
            } else {
                // 文件不存在，返回首页
                fileInputStream = this.customGetInputStreamOfIndex("/front/index.html");
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


    private InputStream customGetInputStream(Resource resource) {
        InputStream is;
        try {
            is = resource.getInputStream();
        } catch (IOException e) {
            // e.printStackTrace();
            is = null;
        }
        return is;
    }

    private boolean customIsFile(String uri) {
        boolean flag = false;
        String lastPath = uri.substring(uri.lastIndexOf("/") + 1);
        if(lastPath != null && lastPath.lastIndexOf(".") != -1) {
            String suffix = lastPath.substring(lastPath.lastIndexOf(".") + 1);
            if(suffix.length() > 0) {
                flag = true;
            }
        }
        return flag;
    }

    private InputStream customGetInputStreamOfIndex(String indexPath) throws IOException {
        ClassPathResource pathResource = new ClassPathResource(indexPath);
        // fileInputStream = new FileInputStream(pathResource.getFile());
        return pathResource.getInputStream();
    }
}
