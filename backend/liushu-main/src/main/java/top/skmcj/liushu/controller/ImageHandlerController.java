package top.skmcj.liushu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.ImageContentTypeEnum;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.entity.Image;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/img")
public class ImageHandlerController {

    @Value("${liushu.oss}")
    private String rootPath;

    @PostMapping("/upload")
    public Result<Image> upload(MultipartFile file, @RequestParam String type, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        Long size = file.getSize();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 生成保存文件名
        String uri = type + "-" + UUID.randomUUID().toString() + suffix;
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/img/" + uri;
        // 返回数据
        Image image = new Image();
        image.setName(fileName);
        image.setType(contentType);
        image.setSize(size);
        image.setUrl(url);
        image.setUri(uri);
        boolean flag = this.saveImage(type, uri, file);
        if(flag) {
            return Result.success(image, StatusCodeEnum.IMAGE_UPLOAD_OK);
        }else {
            return Result.error(image, StatusCodeEnum.IMAGE_UPLOAD_OK);
        }
    }

    @GetMapping("/{imgName}")
    public void getImage(@PathVariable("imgName") String name, HttpServletResponse response) {
        /**
         * bkc - book_cover
         * sta - store_about
         */
        String dirName = this.getImageDirName(name);
        String contentType = this.getImageContentType(name);
        String imgPath = rootPath + dirName + name;
        try {
            // 输入流
            FileInputStream inputStream = new FileInputStream(imgPath);
            // 输出流
            ServletOutputStream outputStream = response.getOutputStream();

            // 设置返回文件类型
            response.setContentType(contentType);

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            // 关闭资源
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件所在的目录
     * @param imgName
     * @return
     */
    public String getImageDirName(String imgName) {
        String prefix = imgName.split("-")[0];
        String name;
        switch (prefix) {
            case "bkc":
                name = "book_cover/";
                break;
            case "sta":
                name = "store_about/";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }

    /**
     * 根据图片名获取图片ContentType
     * @param imgName
     * @return
     */
    public String getImageContentType(String imgName) {
        String suffix = imgName.substring(imgName.lastIndexOf(".") + 1);
        return ImageContentTypeEnum.getTypeOfContentType(suffix);
    }

    /**
     * 保存图片
     * @param type
     * @param uri
     * @return
     */
    public Boolean saveImage(String type, String uri, MultipartFile file) {
        String imgPath;
        switch (type) {
            case "bkc":
                imgPath = rootPath + "book_cover/";
                break;
            case "sta":
                imgPath = rootPath + "store_about/";
                break;
            default:
                imgPath = rootPath + "other/";
                break;
        }
        // 判断文件目录路径是否存在
        File dir = new File(imgPath);
        if(!dir.exists()) {
            // 不存在
            dir.mkdirs();
        }
        // 保存图片
        try {
            file.transferTo(new File(imgPath + uri));
        } catch (IOException e) {
            e.printStackTrace();
            // 出错，返回失败
            return false;
        }
        // 无误，返回成功
        return true;
    }
}
