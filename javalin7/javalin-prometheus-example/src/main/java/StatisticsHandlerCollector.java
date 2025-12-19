import io.prometheus.client.Collector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.jetty.server.handler.StatisticsHandler;

public class StatisticsHandlerCollector extends Collector {

    private final StatisticsHandler statisticsHandler;
    private static final List<String> EMPTY_LIST = new ArrayList<>();

    private StatisticsHandlerCollector(StatisticsHandler statisticsHandler) {
        this.statisticsHandler = statisticsHandler;
    }

    public static void initialize(StatisticsHandler statisticsHandler) {
        new StatisticsHandlerCollector(statisticsHandler).register();
    }

    @Override
    public List<MetricFamilySamples> collect() {
        return Arrays.asList(
            // Requests
            buildCounter("jetty_requests_total", "Number of requests", statisticsHandler.getRequestTotal()),
            buildGauge("jetty_requests_active", "Number of requests currently active", statisticsHandler.getRequestsActive()),
            buildGauge("jetty_requests_active_max", "Maximum number of requests that have been active at once", statisticsHandler.getRequestsActiveMax()),
            buildGauge("jetty_request_time_max_seconds", "Maximum time spent handling requests", statisticsHandler.getRequestTimeMax() / 1_000_000_000.0),
            buildCounter("jetty_request_time_seconds_total", "Total time spent in all request handling", statisticsHandler.getRequestTimeTotal() / 1_000_000_000.0),
            buildCounter("jetty_request_time_mean_seconds", "Mean time spent handling requests", statisticsHandler.getRequestTimeMean() / 1_000_000_000.0),
            buildCounter("jetty_request_time_stddev_seconds", "Standard deviation of time spent handling requests", statisticsHandler.getRequestTimeStdDev() / 1_000_000_000.0),
            // Handler execution (new in Jetty 12)
            buildCounter("jetty_handle_total", "Total number of calls to handle()", statisticsHandler.getHandleTotal()),
            buildGauge("jetty_handle_active", "Current number of requests in handle()", statisticsHandler.getHandleActive()),
            buildGauge("jetty_handle_active_max", "Maximum number of requests in handle()", statisticsHandler.getHandleActiveMax()),
            buildGauge("jetty_handle_time_max_seconds", "Maximum handle() execution time", statisticsHandler.getHandleTimeMax() / 1_000_000_000.0),
            buildCounter("jetty_handle_time_seconds_total", "Total time spent in handle() execution", statisticsHandler.getHandleTimeTotal() / 1_000_000_000.0),
            buildCounter("jetty_handle_time_mean_seconds", "Mean handle() execution time", statisticsHandler.getHandleTimeMean() / 1_000_000_000.0),
            buildCounter("jetty_handle_time_stddev_seconds", "Standard deviation of handle() execution time", statisticsHandler.getHandleTimeStdDev() / 1_000_000_000.0),
            // Failures
            buildCounter("jetty_failures_total", "Number of failed requests", statisticsHandler.getFailures()),
            buildCounter("jetty_handling_failures_total", "Number of requests that threw an exception from handle()", statisticsHandler.getHandlingFailures()),
            // Bytes
            buildCounter("jetty_bytes_read_total", "Total bytes read", statisticsHandler.getBytesRead()),
            buildCounter("jetty_bytes_written_total", "Total bytes written", statisticsHandler.getBytesWritten()),
            // Statistics duration
            buildGauge("jetty_stats_seconds", "Time in seconds stats have been collected for", statisticsHandler.getStatisticsDuration().toMillis() / 1000.0),
            // Response status codes
            buildStatusCounter()
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

    private static MetricFamilySamples buildCounter(String name, String help, double value) {
        return new MetricFamilySamples(
            name,
            Type.COUNTER,
            help,
            Collections.singletonList(new MetricFamilySamples.Sample(name, EMPTY_LIST, EMPTY_LIST, value))
        );
    }

    private MetricFamilySamples buildStatusCounter() {
        String name = "jetty_responses_total";
        return new MetricFamilySamples(
            name,
            Type.COUNTER,
            "Number of requests with response status",
            Arrays.asList(
                buildStatusSample(name, "1xx", statisticsHandler.getResponses1xx()),
                buildStatusSample(name, "2xx", statisticsHandler.getResponses2xx()),
                buildStatusSample(name, "3xx", statisticsHandler.getResponses3xx()),
                buildStatusSample(name, "4xx", statisticsHandler.getResponses4xx()),
                buildStatusSample(name, "5xx", statisticsHandler.getResponses5xx())
            )
        );
    }

    private static MetricFamilySamples.Sample buildStatusSample(String name, String status, double value) {
        return new MetricFamilySamples.Sample(
            name,
            Collections.singletonList("code"),
            Collections.singletonList(status),
            value
        );
    }

}
