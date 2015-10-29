package net.contargo.iris.monitoring;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * @author David Schilling - schilling@synyx.de
 */
public class PerformanceStats {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private final StatsDClient statsd;
    private final boolean enabled;

    public PerformanceStats(String statsdHost, Integer statsdPort, String environment) {

        if (StringUtils.isEmpty(statsdHost) || statsdPort == null) {
            enabled = false;
            statsd = null;
        } else {
            enabled = true;

            String monitoringKey = "iris-" + environment;
            LOG.info("Monitoring key: " + monitoringKey);

            statsd = new NonBlockingStatsDClient(monitoringKey, statsdHost, statsdPort);
        }
    }

    public void updateStats(String methodName, long elapsedTime) {

        if (enabled) {
            statsd.recordExecutionTime(methodName, elapsedTime);
        }
    }
}
