import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

public class JavalinMdnsExampleApp {

    private static final String HOSTNAME = "javalin-demo";
    private static final int PORT = 80;
    private static final Logger log = LoggerFactory.getLogger("JavalinMdnsExampleApp");

    private static JmDNS jmdns;

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.routes.get("/", ctx -> ctx.result("mDNS demo server is running. Served by " + HOSTNAME + ".local"));
            config.events.serverStarted(() -> jmdns = startMdns());
            config.events.serverStopping(() -> stopMdns(jmdns));
        });
        app.start(PORT);
    }

    private static JmDNS startMdns() {
        try {
            InetAddress address = selectAddress();
            JmDNS jmdns = JmDNS.create(address, HOSTNAME);
            log.info("mDNS hostname published: {}.local -> {}", HOSTNAME, address.getHostAddress());
            ServiceInfo service = ServiceInfo.create("_http._tcp.local.", HOSTNAME, PORT, "path=/");
            jmdns.registerService(service);
            log.info("mDNS service registered: {} (_http._tcp) on port {}", HOSTNAME, PORT);
            return jmdns;
        } catch (Exception e) {
            log.warn("Failed to start mDNS responder", e);
            return null;
        }
    }

    private static void stopMdns(JmDNS jmdns) {
        if (jmdns == null) return;
        try {
            jmdns.unregisterAllServices();
            jmdns.close();
        } catch (Exception e) {
            log.warn("Failed to stop mDNS responder", e);
        }
    }

    // Pick a real LAN interface; InetAddress.getLocalHost() can resolve to loopback, which breaks mDNS multicast.
    private static InetAddress selectAddress() throws Exception {
        for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            if (!ni.isUp() || ni.isLoopback() || ni.isVirtual() || ni.isPointToPoint()) continue;
            for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
                if (addr instanceof Inet4Address && addr.isSiteLocalAddress()) return addr;
            }
        }
        InetAddress fallback = InetAddress.getLocalHost();
        if (fallback.isLoopbackAddress()) {
            log.warn("No non-loopback site-local interface found; mDNS bound to {} and multicast may not work", fallback.getHostAddress());
        }
        return fallback;
    }

}
