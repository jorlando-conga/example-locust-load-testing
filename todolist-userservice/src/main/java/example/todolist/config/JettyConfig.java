package example.todolist.config;

import example.todolist.filter.SessionFilter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextListener;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JettyConfig {

    private static final int DEFAULT_PORT = 8080;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(JettyConfig.class);

    @Value("${http.port}")
    private String httpPortStr;

    @Bean
    public WebServerFactoryCustomizer<JettyServletWebServerFactory> webServerFactoryCustomizer() {
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("dirAllowed", Boolean.FALSE.toString());
        return factory -> {
            int port = DEFAULT_PORT;
            try {
                port = Integer.valueOf(httpPortStr);
            } catch (Exception e) {
                LOG.error("Unable to apply property $PORT to Jetty Web Server Factory", e);
            }
            factory.setPort(port);
            factory.setInitParameters(initParameters);
        };
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilterFilterRegistrationBean(SessionFilter sessionFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(sessionFilter);
        registrationBean.setEnabled(true);
        registrationBean.setUrlPatterns(Arrays.asList("/*"));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
