package icu.shishc.consts;

import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ZkConstants {
    // hostname = "1.1.1.1"
    public static String hostname = "your_ip_address";

    public static String zkPath;

    static {
        InetAddress host;
        try {
            host = InetAddress.getLocalHost();
            System.out.println("hostname: " + host.toString());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // eg. zkPath = "Client@Client_172.0.0.1"
        zkPath = "Client@Client_" + host.getHostAddress();
        System.out.println("zkPath: " + zkPath);
    }
}
