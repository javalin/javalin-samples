import io.javalin.Javalin
import org.slf4j.LoggerFactory
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

private const val HOSTNAME = "javalin-demo"
private const val PORT = 80
private val log = LoggerFactory.getLogger("JavalinMdnsExampleApp")

private var jmdns: JmDNS? = null

fun main() {
    val app = Javalin.create { config ->
        config.routes.get("/") { it.result("mDNS demo server is running. Served by $HOSTNAME.local") }
        config.events.serverStarted { jmdns = startMdns() }
        config.events.serverStopping { stopMdns(jmdns) }
    }
    app.start(PORT)
}

private fun startMdns(): JmDNS? = try {
    val address = selectAddress()
    val jmdns = JmDNS.create(address, HOSTNAME)
    log.info("mDNS hostname published: {}.local -> {}", HOSTNAME, address.hostAddress)
    val service = ServiceInfo.create("_http._tcp.local.", HOSTNAME, PORT, "path=/")
    jmdns.registerService(service)
    log.info("mDNS service registered: {} (_http._tcp) on port {}", HOSTNAME, PORT)
    jmdns
} catch (e: Exception) {
    log.warn("Failed to start mDNS responder", e)
    null
}

private fun stopMdns(jmdns: JmDNS?) {
    if (jmdns == null) return
    try {
        jmdns.unregisterAllServices()
        jmdns.close()
    } catch (e: Exception) {
        log.warn("Failed to stop mDNS responder", e)
    }
}

// Pick a real LAN interface; InetAddress.getLocalHost() can resolve to loopback, which breaks mDNS multicast.
private fun selectAddress(): InetAddress {
    val siteLocal = NetworkInterface.getNetworkInterfaces().asSequence()
        .filter { it.isUp && !it.isLoopback && !it.isVirtual && !it.isPointToPoint }
        .flatMap { it.inetAddresses.asSequence() }
        .firstOrNull { it is Inet4Address && it.isSiteLocalAddress }
    if (siteLocal != null) return siteLocal
    val fallback = InetAddress.getLocalHost()
    if (fallback.isLoopbackAddress) {
        log.warn("No non-loopback site-local interface found; mDNS bound to {} and multicast may not work", fallback.hostAddress)
    }
    return fallback
}
