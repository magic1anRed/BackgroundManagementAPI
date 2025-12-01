package features;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.magic.backgroundmanagement.App;

import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.service.LoginService;
import cn.magic.backgroundmanagement.utils.Ip2RegionUtil;
import cn.magic.backgroundmanagement.utils.MD5SaltsUtil;
import cn.magic.backgroundmanagement.utils.SaTokenConfig;
import com.easy.query.core.api.client.EasyQueryClient;
import com.easy.query.solon.annotation.Db;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.jupiter.api.Test;

import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SolonTest(App.class)
public class HelloTest extends HttpTester {

    @Db
    private EasyQueryClient easyEntityQuery;

    @Inject
    private LoginService loginService;

    @Inject
    SaTokenDao saTokenDao;
    @Test
    public void hello() throws IOException {

/*        String ip1 = "192.168.3.1";
        String ip2 = "localhost";
        String ip3 = "27.154.86.48";
        String ip4 = "211.99.98.197";
        String ip5 = "112.5.16.1";
        String ip6 = "2001:4860:4860::8888";
        String ip7 = "2606:4700:4700::1111";

        String region = Ip2RegionUtil.getRegion("180.76.76.76");
        System.out.println("region = " + region);*/
/*        String salts = MD5SaltsUtil.salts();
        String s = MD5SaltsUtil.md5("679523",salts);
        System.out.println("password = " + s + " | salts = " + salts);*/
        String ts = String.valueOf(System.currentTimeMillis());
        System.out.println("当前时间戳: " + ts);

        // 2. 生成 token（Base64 编码方式）
        String token = Base64.getEncoder().encodeToString(ts.getBytes(StandardCharsets.UTF_8));
        System.out.println("生成的 token: " + token);

        // 3. 使用 SaTokenDao 写入 Redis，有效期 3600 秒
        saTokenDao.set("test:token", token, 3600);

        System.out.println("Token 已使用 SaTokenDao 成功写入 Redis！");



    }
}