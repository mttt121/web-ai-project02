package org.example;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.ListBucketsRequest;
import com.aliyun.sdk.service.oss2.models.ListBucketsResult;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;

/**
 * OSS 上传问题定位工具（不是业务代码，查完可以删）
 * 直接右键运行 main 方法即可，三步分别验证：凭证是否读到、AK/SK 是否配对、上传是否可用。
 */
public class OssCheck {

    private static final String REGION = "cn-beijing";
    private static final String BUCKET = "java-web-ai-mt";

    public static void main(String[] args) {
        String ak = System.getenv("OSS_ACCESS_KEY_ID");
        String sk = System.getenv("OSS_ACCESS_KEY_SECRET");

        System.out.println("==== 1. 凭证是否读到 ====");
        System.out.println("AK: " + mask(ak) + "  长度=" + len(ak));
        System.out.println("SK: " + mask(sk) + "  长度=" + len(sk));
        System.out.println("（AK 正常 24 位，SK 正常 30 位；为 null 说明启动环境里没有这两个变量）");

        try (OSSClient client = OSSClient.newBuilder()
                .credentialsProvider(new EnvironmentVariableCredentialsProvider())
                .region(REGION)
                .build()) {

            System.out.println("\n==== 2. listBuckets 验证 AK/SK 是否配对 ====");
            try {
                ListBucketsResult result = client.listBuckets(ListBucketsRequest.newBuilder().build());
                System.out.println("✅ 鉴权通过，账号下共 " + result.buckets().size() + " 个 Bucket");
                result.buckets().forEach(b -> System.out.println("   - " + b.name() + " (" + b.region() + ")"));
            } catch (Exception e) {
                System.out.println("❌ 鉴权失败：" + rootMsg(e));
                System.out.println("   若为 SignatureDoesNotMatch / InvalidAccessKeyId，基本可确定 AK 与 SK 不配对");
            }

            System.out.println("\n==== 3. 上传纯 ASCII 对象（不带任何中文 header） ====");
            try {
                PutObjectResult result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(BUCKET)
                        .key("check/hello.txt")
                        .contentType("text/plain")
                        .body(BinaryData.fromString("hello oss"))
                        .build());
                System.out.println("✅ 上传成功，status=" + result.statusCode());
            } catch (Exception e) {
                System.out.println("❌ 上传失败：" + rootMsg(e));
            }

            System.out.println("\n==== 4. 带中文自定义 header 上传（复现原问题） ====");
            try {
                PutObjectResult result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(BUCKET)
                        .key("check/hanzi.txt")
                        .contentType("text/plain")
                        .metadata(java.util.Map.of("original-name", "测试图片.png"))
                        .body(BinaryData.fromString("hello oss"))
                        .build());
                System.out.println("✅ 上传成功，说明中文 header 不是原因，status=" + result.statusCode());
            } catch (Exception e) {
                System.out.println("❌ 上传失败（复现成功，中文 header 就是根因）：" + rootMsg(e));
            }

            System.out.println("\n==== 5. 中文文件名 URL 编码后再上传（修复方案验证） ====");
            try {
                PutObjectResult result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(BUCKET)
                        .key("check/hanzi-encoded.txt")
                        .contentType("text/plain")
                        .metadata(java.util.Map.of("original-name",
                                java.net.URLEncoder.encode("测试图片.png", java.nio.charset.StandardCharsets.UTF_8)))
                        .body(BinaryData.fromString("hello oss"))
                        .build());
                System.out.println("✅ 上传成功，最终方案可用，status=" + result.statusCode());
            } catch (Exception e) {
                System.out.println("❌ 上传失败：" + rootMsg(e));
            }

            System.out.println("\n==== 6. 清理第 3、5 步产生的测试对象 ====");
            for (String k : new String[]{"check/hello.txt", "check/hanzi-encoded.txt"}) {
                try {
                    client.deleteObject(com.aliyun.sdk.service.oss2.models.DeleteObjectRequest.newBuilder()
                            .bucket(BUCKET).key(k).build());
                    System.out.println("已删除 " + k);
                } catch (Exception e) {
                    System.out.println("删除 " + k + " 失败：" + rootMsg(e));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String rootMsg(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        return t.getClass().getSimpleName() + ": " + t.getMessage();
    }

    private static String mask(String v) {
        if (v == null) return "null(未读到)";
        if (v.length() <= 8) return "***";
        return v.substring(0, 6) + "****" + v.substring(v.length() - 4);
    }

    private static int len(String v) {
        return v == null ? 0 : v.length();
    }
}
