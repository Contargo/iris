package net.contargo.iris.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

import java.lang.invoke.MethodHandles;


/**
 * Needed to add property files to the environment.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class ContargoApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String ENVIRONMENT = "environment";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        try {
            String env = System.getenv(ENVIRONMENT);

            if (env == null) {
                env = System.getProperty(ENVIRONMENT, "dev");
            }

            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            environment.getPropertySources()
                .addLast(new ResourcePropertySource("classpath:application-" + env + ".properties"));
            environment.getPropertySources().addLast(new ResourcePropertySource("classpath:application.properties"));
        } catch (IOException ex) {
            LOG.warn("Unable to load application properties!", ex);
        }
    }
}
