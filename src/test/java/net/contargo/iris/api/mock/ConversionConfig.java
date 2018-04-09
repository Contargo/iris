package net.contargo.iris.api.mock;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route2.api.RoutePartNodeDto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;

import org.springframework.core.convert.converter.Converter;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class ConversionConfig {

    @Bean
    public ConversionServiceFactoryBean conversionService() {

        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(converters());

        return bean;
    }


    private Set<Converter<?, ?>> converters() {

        Converter<RoutePartNodeDto, GeoLocation> nodeDtoToGeoLocationConverter =
            new Converter<RoutePartNodeDto, GeoLocation>() {

                @Override
                public GeoLocation convert(RoutePartNodeDto nodeDto) {

                    return new GeoLocation();
                }
            };

        return Stream.of(nodeDtoToGeoLocationConverter).collect(toSet());
    }
}
