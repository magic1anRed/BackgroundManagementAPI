package features;

import cn.magic.backgroundmanagement.App;

import cn.magic.backgroundmanagement.utils.ClientipUtil;
import cn.magic.backgroundmanagement.utils.Ip2RegionUtil;
import cn.magic.backgroundmanagement.utils.MD5SaltsUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.jupiter.api.Test;

import org.noear.solon.annotation.Inject;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

import java.io.IOException;

@SolonTest(App.class)
public class HelloTest extends HttpTester {


    private static final Logger logger = LoggerFactory.getLogger(ClientipUtil.class);

    @Test
    public void hello() throws IOException {
        String ip1 = "192.168.3.1";
        String ip2 = "localhost";
        String ip3 = "27.154.86.48";
        String ip4 = "211.99.98.197";
        String ip5 = "112.5.16.1";

        String s = MD5SaltsUtil.md5("123456", "magic");
        System.out.println("s = " + s);
    }
}