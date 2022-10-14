package top.skmcj.liushu.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
public class VueRouterController {
    /**
     * 处理商家后台页面的跳转请求
     */
    @GetMapping("/store")
    public void gotoStoreIndex(HttpServletResponse response) {
        try {
            ClassPathResource pathResource = new ClassPathResource("store/index.html");
            // 输入流
            FileInputStream fileInputStream = new FileInputStream(pathResource.getFile());
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
}
