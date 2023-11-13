package dev.bayun.ms.users.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.starter.restobject.RestDocument;
import dev.bayun.starter.restobject.RestObjectRegistrarModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Максим Яськов
 */

public class TestResultMatchers {

    private static final ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new RestObjectRegistrarModule("dev.bayun.ms.users"));
    }

    public static RestResultMatchers rest() {
        return new RestResultMatchers();
    }

    public static class RestResultMatchers {

        public ResultMatcher document(RestDocument expected) {
            return result -> {
                MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON).match(result);
                String actualDocumentAsString = result.getResponse().getContentAsString();
                RestDocument actual = OBJECT_MAPPER.readValue(actualDocumentAsString, RestDocument.class);
                assertEquals(expected, actual);
            };
        }

    }

}
