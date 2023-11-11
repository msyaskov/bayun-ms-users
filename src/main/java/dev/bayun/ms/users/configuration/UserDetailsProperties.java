package dev.bayun.ms.users.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author Максим Яськов
 */
@Data
@ConfigurationProperties("ms.users")
public class UserDetailsProperties {

    private Map<String, Details> details;

    @Data
    public static class Details {

        private String[] authorities;

        private String password;

        private boolean enabled;

        public void setAuthorities(String authorities) {
            this.authorities = StringUtils.commaDelimitedListToStringArray(authorities);
        }
    }

}
