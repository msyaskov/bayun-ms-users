package dev.bayun.ms.users.rest;

import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/**
 * @author Максим Яськов
 */
@Configuration
public class RestTemplateConfiguration {

//    @Bean
//    @Profile("local")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, SslBundles sslBundles) {
        return restTemplateBuilder.setSslBundle(sslBundles.getBundle("ssl-server")).build();
    }

}
