import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.GaugeMetricFamily;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class QueuedThreadPoolCollector extends Collector {

    private QueuedThreadPool queuedThreadPool;
    private static final List<String> EMPTY_LIST = new ArrayList<>();

    private QueuedThreadPoolCollector(QueuedThreadPool queuedThreadPool) {
        this.queuedThreadPool = queuedThreadPool;
    }

    public static void initialize(QueuedThreadPool queuedThreadPool) {
        new QueuedThreadPoolCollector(queuedThreadPool).register();
    }

    @Override
    public List<MetricFamilySamples> collect() {
        return Arrays.asList(
            buildGauge("jetty_queued_thread_pool_threads", "Number of total threads", queuedThreadPool.getThreads()),
            buildGauge("jetty_queued_thread_pool_utilization", "Percentage of threads in use", (double)queuedThreadPool.getThreads() / queuedThreadPool.getMaxThreads()),
            buildGauge("jetty_queued_thread_pool_threads_idle", "Number of idle threads", queuedThreadPool.getIdleThreads()),
            buildGauge("jetty_queued_thread_pool_jobs", "Number of total jobs", queuedThreadPool.getQueueSize())
        );
    }

    private static MetricFamilySamples buildGauge(String name, String help, double value) {
        return new MetricFamilySamples(
            name,
            Type.GAUGE,
            help,
            Collections.singletonList(new MetricFamilySamples.Sample(name, EMPTY_LIST, EMPTY_LIST, value))
        );
    }
}
