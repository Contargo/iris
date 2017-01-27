package net.contargo.iris.config;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.jmx.support.MBeanServerFactoryBean;

import java.util.Arrays;

import javax.management.MBeanServer;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Configuration
public class CacheMBeanConfiguration {

    @Bean
    public MBeanServerFactoryBean mbeanServer() {

        MBeanServerFactoryBean mBeanServerFactoryBean = new MBeanServerFactoryBean();
        mBeanServerFactoryBean.setLocateExistingServerIfPossible(true);

        return mBeanServerFactoryBean;
    }


    @Bean
    public String dummyBean(MBeanServer mBeanServer, CacheManager cacheManager) {

        // This check is only relevant for running integration tests which reuse a application context
        if (!Arrays.asList(mBeanServer.getDomains()).contains("net.sf.ehcache")) {
            ManagementService.registerMBeans(cacheManager, mBeanServer, true, true, true, true, true);
        }

        return "dummyBean";
    }
}
