package org.example.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class AliyunOSSOperator implements AutoCloseable {

    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    private OSSClient client;

    /**
     * @PostConstruct：Spring 把依赖注入完成之后，立刻调用一次这个方法。
     * 这里创建 OSSClient，整个应用共用这一个实例。
     */
    @PostConstruct
    public void init() {
        log.info(">>>>>>>>>> 初始化 OSSClient：region={}, bucket={} <<<<<<<<<<",
                aliyunOSSProperties.getRegion(), aliyunOSSProperties.getBucket());
        this.client = OSSClient.newBuilder()
                .credentialsProvider(new EnvironmentVariableCredentialsProvider())
                .region(aliyunOSSProperties.getRegion())
                .build();
        log.info(">>>>>>>>>> OSSClient 创建完成，之后整个应用共用这一个 <<<<<<<<<<");
    }

    /**
     * 上传文件到阿里云OSS（Bucket为公开读），返回永久可访问的固定链接
     *
     * @param file 需要上传的文件
     * @return 文件的访问地址
     */
    public String upload(MultipartFile file) throws Exception {
        // 用 UUID 做文件名，避免重名覆盖和中文名乱码
        String originalFilename = file.getOriginalFilename();
        String ext = extractExtension(originalFilename);
        String key = "images/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;

        // 兜底：浏览器有时不传 Content-Type
        String contentType = file.getContentType();
        if (contentType == null || contentType.isEmpty()) contentType = "image/jpeg";

        try (InputStream in = file.getInputStream()) {

            PutObjectResult result = client.putObject(PutObjectRequest.newBuilder()
                    .bucket(aliyunOSSProperties.getBucket())
                    .key(key)
                    .contentType(contentType)
                    .contentLength(Math.toIntExact(file.getSize()))
                    // 自定义 header 必须是纯 ASCII，中文文件名会让 V4 签名对不上 -> 403 SignatureDoesNotMatch
                    .metadata(Map.of("original-name",
                            URLEncoder.encode(originalFilename == null ? "" : originalFilename,
                                    StandardCharsets.UTF_8)))
                    .body(BinaryData.fromStream(in))
                    .build());

            if (result.statusCode() != 200) {
                throw new IOException("OSS 上传失败, status=" + result.statusCode());
            }
        }

        // Bucket 为公开读，直接返回固定访问地址
        return "https://" + aliyunOSSProperties.getBucket() + "." + endpointHost() + "/" + key;
    }

    /**
     * 拼出 Bucket 的访问域名：bucket.oss-cn-beijing.aliyuncs.com
     * 注意 region（cn-beijing）前面必须补 oss-，否则链接打不开
     */
    private String endpointHost() {
        String r = aliyunOSSProperties.getRegion().startsWith("oss-") ? aliyunOSSProperties.getRegion().substring(4) : aliyunOSSProperties.getRegion();
        return "oss-" + r + ".aliyuncs.com";
    }

    /**
     * 从文件名中提取扩展名（不含点），无扩展名时默认 png
     */
    private String extractExtension(String name) {
        if (name != null && name.contains(".")) {
            return name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        }
        return "png";
    }

    @Override
    public void close() throws Exception {
        log.info(">>>>>>>>>> 应用关闭，这个 Bean 被销毁 <<<<<<<<<<");
        if (client != null) {
            client.close();
        }
    }
}