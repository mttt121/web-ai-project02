package org.example.controller;

import org.example.pojo.Result;
import org.example.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
public class UploadController {


    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;


    /**
     * 上传文件 - 参数名file，上传到阿里云OSS
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        log.info("上传文件：{}, 大小：{} 字节", file.getOriginalFilename(), file.getSize());
        try {
            String url = aliyunOSSOperator.upload(file);
            return Result.success(url);
        } catch (Exception e) {
            // 把真实原因打进日志，前端也能拿到提示，而不是一个光秃秃的 500
            log.error("文件上传失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
