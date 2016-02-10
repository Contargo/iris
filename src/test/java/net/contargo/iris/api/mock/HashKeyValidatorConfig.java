package net.contargo.iris.api.mock;

import net.contargo.iris.address.staticsearch.validator.HashKeyValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


@Configuration
public class HashKeyValidatorConfig {

    @Bean
    public HashKeyValidator hashKeyValidator() {

        return mock(HashKeyValidator.class);
    }
}
