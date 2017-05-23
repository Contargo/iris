package net.contargo.iris.routedatarevision.service.cleanup;

import net.contargo.iris.routedatarevision.web.RouteDataRevisionCleanupRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteDataRevisionCleanupTask {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RouteDataRevisionCleanupService cleanupService;

    private final AtomicBoolean cleanupRunning = new AtomicBoolean(false);

    public RouteDataRevisionCleanupTask(RouteDataRevisionCleanupService cleanupService) {

        this.cleanupService = cleanupService;
    }

    public void submit(RouteDataRevisionCleanupRequest cleanupRequest) {

        if (cleanupRunning.compareAndSet(false, true)) {
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    LOG.info("Starting route data revision cleanup");
                    cleanupService.cleanup(cleanupRequest);
                } catch (RuntimeException e) {
                    LOG.error(e.getMessage(), e);
                } finally {
                    cleanupRunning.set(false);
                    LOG.info("Route data revision cleanup finished");
                }
            });
        }
    }


    public boolean isRunning() {

        return cleanupRunning.get();
    }
}
