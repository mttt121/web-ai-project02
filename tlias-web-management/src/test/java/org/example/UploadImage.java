package org.example;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.PresignOptions;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.*;
import com.aliyun.sdk.service.oss2.transport.BinaryData;

import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;

public class UploadImage {
    public static void main(String[] args) throws Exception {
        String region = "cn-beijing";       // ★ 你的地域
        String bucket = "java-web-ai-mt";       // ★ 你的 Bucket
        Path image = Paths.get("D:/images/1.png");   // ★ 你的图片，建议绝对路径

        // 用 UUID 做文件名，避免重名覆盖和中文名乱码
        String name = image.getFileName().toString();
        String ext = name.contains(".") ? name.substring(name.lastIndexOf('.') + 1).toLowerCase() : "png";
        String key = "images/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;


        // 兜底：Windows 上 probeContentType 有时返回 null
        String contentType = Files.probeContentType(image);
        if (contentType == null) contentType = "image/jpeg";

        try (OSSClient client = OSSClient.newBuilder()
                .credentialsProvider(new EnvironmentVariableCredentialsProvider())
                .region(region)
                .build();
             InputStream in = Files.newInputStream(image)) {

            PutObjectResult result = client.putObject(PutObjectRequest.newBuilder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .contentLength((int) Files.size(image))
                    .metadata(java.util.Map.of("original-name", name))  // 原文件名存起来
                    .body(BinaryData.fromStream(in))
                    .build());

            System.out.println("上传成功: " + key + " / status=" + result.statusCode());

            // 私有 Bucket 必须用临时 URL，直接拼域名会 403
            System.out.println("访问地址: " + client.presign(
                    GetObjectRequest.newBuilder().bucket(bucket).key(key).build(),
                    PresignOptions.newBuilder().expiration(Instant.now().plusSeconds(3600)).build()
            ).url());
        }
    }
}
