package cn.magic.backgroundmanagement.utils;

import cn.hutool.core.io.IoUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.UploadedFile;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException; // 导入必要的类
import java.net.URLEncoder; // 导入必要的类

@Component
public class QiniuUtils {

    // 建议放 application.yml (Solon)
    private final String accessKey = "W3rtzw0abHGilqW1TpQZMZA5-OIiG1MMhfMQRku9";
    private final String secretKey = "-KR4keUuWEAvFqno8u9s-YXmdlnXVuVxjugqXO-a";
    private final String bucket = "avatarsave";

    // 测试域名必须用 HTTP
    private final String domain = "http://t6bcqea8l.hn-bkt.clouddn.com";

    private final UploadManager uploadManager;

    public QiniuUtils() {
        // 华南：Region.region2()
        Configuration cfg = new Configuration(Region.region2());
        uploadManager = new UploadManager(cfg);
    }

    /**
     * 上传文件到七牛
     * @param file Solon 的 UploadedFile
     * @return 文件访问 URL
     */
    public String upload(UploadedFile file) {
        if (file == null) {
            throw new RuntimeException("上传文件不能为空！");
        }

        String key = "avatar_" + System.currentTimeMillis() + "_" + file.getName();

        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(bucket);

        try (InputStream in = file.getContent()) {

            byte[] bytes = IoUtil.readBytes(in);

            Response response = uploadManager.put(bytes, key, uploadToken);

            if (response.isOK()) {
                // ****** 修复 URL 编码问题 ******
                try {
                    // 对 key 进行 URL 编码，以确保文件名中的特殊字符可以被正确解析。
                    // URLEncoder 默认将空格编码为 '+'，但 URL 路径通常需要 %20，因此进行替换。
                    String encodedKey = URLEncoder.encode(key, "UTF-8").replace("+", "%20");
                    //将key存入数据库

                    return domain + "/" + encodedKey;
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("URL 编码失败 (UTF-8 不支持?)", e);
                }
                // ******************************
            } else {
                throw new RuntimeException("七牛上传失败：" + response.toString());
            }

        } catch (QiniuException e) {
            throw new RuntimeException("七牛异常：" + e.response.toString(), e);
        } catch (IOException e) {
            throw new RuntimeException("文件读取异常", e);
        }
    }
}