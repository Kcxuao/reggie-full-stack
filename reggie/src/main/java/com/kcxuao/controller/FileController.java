package com.kcxuao.controller;

import com.kcxuao.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 */
@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${reggie.dirPath}")
    private String dirPath;

    /**
     * 文件上传
     * @param file  文件流
     * @return 文件名
     */
    @PostMapping
    public R<String> upload(MultipartFile file) {
        log.info("文件上传 ==> {}", file.toString());

        // 获取文件后缀名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // 生成新的文件名
        String fileName = UUID.randomUUID() + suffix;
        
        // 判断目录是否存在，否则创建目录
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        try {
            file.transferTo(new File(dirPath + fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name 文件名
     * @param response 响应
     */
    @GetMapping("/{name}")
    public void download(@PathVariable String name, HttpServletResponse response) {
        log.info("下载图片 ==> {}", name);

        try {
            File file = new File(dirPath + name);

            // 文件不存在返回默认图片
            if (!file.exists()) {
                file = new File(dirPath + "noImg.png");
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            // 返回图片流
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }

            // 关闭管道
            fileInputStream.close();
            outputStream.close();


        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }
}
