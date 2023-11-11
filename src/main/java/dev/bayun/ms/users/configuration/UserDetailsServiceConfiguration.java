package dev.bayun.ms.users.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Максим Яськов
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(UserDetailsProperties.class)
public class UserDetailsServiceConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService inMemoryUserDetailsManager(UserDetailsProperties userDetailsProperties) {
        List<UserDetails> userDetailsList = Objects.requireNonNullElseGet(userDetailsProperties.getDetails(), Map::<String, UserDetailsProperties.Details>of).entrySet()
                .stream()
                .map(this::obtainUserDetails).toList();

        log.info("Registered UserDetails:");
        userDetailsList.forEach(userDetails -> log.info(userDetails.toString()));

        return new InMemoryUserDetailsManager(userDetailsList);
    }

    private UserDetails obtainUserDetails(Map.Entry<String, UserDetailsProperties.Details> entry) {
        String username = entry.getKey();
        UserDetailsProperties.Details details = entry.getValue();
        return User.builder()
                .username(username)
                .password(Objects.requireNonNullElse(details.getPassword(), ""))
                .authorities(details.getAuthorities())
                .disabled(!details.isEnabled())
                .build();
    }

}
