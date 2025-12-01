package features;

import cn.magic.backgroundmanagement.App;

import cn.magic.backgroundmanagement.utils.Ip2RegionUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.jupiter.api.Test;

import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

import java.io.IOException;

@SolonTest(App.class)
public class HelloTest extends HttpTester {


    @Test
    public void hello() throws IOException {
        String ip1 = "192.168.3.1";
        String ip2 = "localhost";
        String ip3 = "27.154.86.48";
        String ip4 = "211.99.98.197";
        String ip5 = "112.5.16.1";
        String ip6 = "2001:4860:4860::8888";
        String ip7 = "2606:4700:4700::1111";

        String region = Ip2RegionUtil.getRegion("180.76.76.76");
        System.out.println("region = " + region);
    }
}