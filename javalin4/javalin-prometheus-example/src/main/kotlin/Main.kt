import com.mashape.unirest.http.Unirest
import com.sun.media.jfxmedia.logging.Logger
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.prometheus.client.exporter.HTTPServer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.StatisticsHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.slf4j.LoggerFactory

fun main() {

    val statisticsHandler = StatisticsHandler()
    val queuedThreadPool = QueuedThreadPool(200, 8, 60_000)

    val app = Javalin.create {
        it.server {
            Server(queuedThreadPool).apply {
                handler = statisticsHandler
            }
        }
    }.start(7070)

    initializePrometheus(statisticsHandler, queuedThreadPool)

    app.routes {
        get("/1") { ctx -> ctx.result("Hello World") }
        get("/2") { ctx ->
            Thread.sleep((Math.random() * 2000).toLong())
            ctx.result("Slow Hello World")
        }
        get("/3") { ctx -> ctx.redirect("/2") }
        get("/4") { ctx -> ctx.status(400) }
        get("/5") { ctx -> ctx.status(500) }
    }

    while (true) {
        spawnRandomRequests()
    }

}

private fun initializePrometheus(statisticsHandler: StatisticsHandler, queuedThreadPool: QueuedThreadPool) {
    StatisticsHandlerCollector.initialize(statisticsHandler)
    QueuedThreadPoolCollector.initialize(queuedThreadPool)
    val prometheusServer = HTTPServer(7080)
    LoggerFactory.getLogger("Main").info("Prometheus is listening on: http://localhost:7080")
}

private fun spawnRandomRequests() {
    Thread {
        for (i in 0 until (0..50).shuffled()[0]) {
            Unirest.get("http://localhost:7070/1").asString() // we want a lot more "200 - OK" traffic
            Unirest.get("http://localhost:7070/" + (1..5).shuffled()[0]).asString() // hit a random (1-5) endpoint
        }
    }.start()
    Thread.sleep((Math.random() * 250).toLong())
}

