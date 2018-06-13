package net.contargo.iris.config;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.ui.Model;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  David Schilling - schilling@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@Configuration
@EnableSwagger
public class SwaggerConfig {

    private final String[] includePattern = new String[] {
        "\\/geocodes.*", "\\/osmaddresses.*", "\\/places.*", "\\/reversegeocode.*", "\\/airlineDistance.*",
        "\\/countries.*", "\\/connections.*", "\\/routedetails.*", "\\/routerevisions.*", "\\/routes.*",
        "\\/seaports.*", "\\/staticaddresses.*", "\\/terminals.*", "\\/simplegeocodes.*", "\\/addresses.*",
        "\\/route.*", "\\/transports.*", "\\/transport.*"
    };

    private SpringSwaggerConfig springSwaggerConfig;

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {

        this.springSwaggerConfig = springSwaggerConfig;
    }


    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {

        springSwaggerConfig.defaultSwaggerPathProvider().setApiResourcePrefix("api");

        return new SwaggerSpringMvcPlugin(springSwaggerConfig).apiInfo(apiInfo())
            .includePatterns(includePattern)
            .ignoredParameterTypes(Model.class);
    }


    private ApiInfo apiInfo() {

        return new ApiInfo("IRIS Backend Api", null, null, null, null, null);
    }
}
