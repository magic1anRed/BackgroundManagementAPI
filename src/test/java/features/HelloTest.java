package features;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.magic.backgroundmanagement.App;

import cn.magic.backgroundmanagement.entity.UsersEntity;
import cn.magic.backgroundmanagement.service.LoginService;
import cn.magic.backgroundmanagement.utils.ClientipUtil;
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

        String ip1 = "192.168.3.1";
        String ip2 = "localhost";
        String ip3 = "27.154.86.48";
        String ip4 = "211.99.98.197";
        String ip5 = "112.5.16.1";
        String ip6 = "2001:4860:4860::8888";
        String ip7 = "2606:4700:4700::1111";

        String region = ClientipUtil.parse(ip3);
        System.out.println("region = " + region);
/*        String salts = MD5SaltsUtil.salts();
        String s = MD5SaltsUtil.md5("679523",salts);
        System.out.println("password = " + s + " | salts = " + salts);*/




    }
}