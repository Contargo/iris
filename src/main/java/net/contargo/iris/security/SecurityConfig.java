package net.contargo.iris.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.ClassPathResource;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.Properties;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int PASSWORD_STRENGTH = 256;
    private static final String ENVIRONMENT = "environment";
    private static final String ADMIN = "ADMIN";
    private static final String API = "/api/**";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(new ShaPasswordEncoder(PASSWORD_STRENGTH));
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/web/login", "/api/info", "/api/health")
            .permitAll()
            .antMatchers("/web/terminals/**", "/web/seaports/**", "/web/staticaddresses/**", "/web/routerevisions/**")
            .hasRole(ADMIN)
            .antMatchers(PUT, API)
            .hasRole(ADMIN)
            .antMatchers(POST, API)
            .hasRole(ADMIN)
            .antMatchers(DELETE, API)
            .hasRole(ADMIN)
            .antMatchers("/web/**", API, "/api-docs/**")
            .authenticated()
            .and()
            .formLogin()
            .usernameParameter("email")
            .passwordParameter("password")
            .failureUrl("/web/login?error=true")
            .loginPage("/web/login")
            .loginProcessingUrl("/web/login/process")
            .and()
            .httpBasic()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(delegatingAuthenticationEntryPoint);
    }


    @Bean
    public InMemoryUserDetailsManager propertyUserDetailsService() throws IOException {

        String env = System.getenv(ENVIRONMENT);

        if (env == null) {
            env = System.getProperty(ENVIRONMENT, "dev");
        }

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocations(new ClassPathResource("usercredentials-" + env + ".properties"));
        propertiesFactoryBean.afterPropertiesSet();

        Properties users = propertiesFactoryBean.getObject();

        return new InMemoryUserDetailsManager(users);
    }


    @Bean
    public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {

        IRISBasicAuthEntryPoint authEntryPoint = new IRISBasicAuthEntryPoint();
        authEntryPoint.setRealmName("IRIS API");

        return authEntryPoint;
    }


    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {

        return new LoginUrlAuthenticationEntryPoint("/web/login");
    }


    @Bean
    @Autowired
    public DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint(BasicAuthenticationEntryPoint basic,
        LoginUrlAuthenticationEntryPoint login) {

        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
        entryPoints.put(new RequestHeaderRequestMatcher("Content-Type", "application/json"), basic);
        entryPoints.put(new NegatedRequestMatcher(new RequestContainingAcceptTextHeaderRequestMatcher()), basic);

        DelegatingAuthenticationEntryPoint delegate = new DelegatingAuthenticationEntryPoint(entryPoints);
        delegate.setDefaultEntryPoint(login);

        return delegate;
    }
}
