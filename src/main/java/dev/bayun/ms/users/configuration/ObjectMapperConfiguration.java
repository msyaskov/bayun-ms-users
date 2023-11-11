package dev.bayun.ms.users.configuration;

import dev.bayun.ms.users.rest.RestObjectMixin;
import dev.bayun.sdk.rest.core.RestObject;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Максим Яськов
 */
@Configuration
public class ObjectMapperConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.mixIn(RestObject.class, RestObjectMixin.class);
    }

}
