package net.contargo.iris.startup;

import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * Looks for event {@link ContextRefreshedEvent}, starts to do tasks that have to be done on application start.
 *
 * @author  Michael Herbold - herbold@synyx.de
 */
public class IrisStartupWatcher implements ApplicationListener<ContextRefreshedEvent> {

    private final StaticAddressService staticAddressService;

    private boolean runOnce;

    public IrisStartupWatcher(StaticAddressService staticAddressService) {

        this.staticAddressService = staticAddressService;
        runOnce = false;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (!runOnce) {
            // added this boolean, because ContextRefreshedEvent is fired currently three times,
            // because this event is fired for each context file separately (applicationContext/web/rest)
            staticAddressService.fillMissingHashKeys();
            runOnce = true;
        }
    }
}
