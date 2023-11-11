package dev.bayun.ms.users.security;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Максим Яськов
 */
@Getter
@ToString(exclude = "credentials")
@EqualsAndHashCode(exclude = "credentials")
public final class AuthenticatedPrincipal implements UserDetails, OAuth2User, CredentialsContainer {

    @Getter
    private final UUID userId;

    private String credentials;

    @Getter(AccessLevel.NONE)
    private final boolean expired;

    @Getter(AccessLevel.NONE)
    private final boolean locked;

    @Getter(AccessLevel.NONE)
    private final boolean credentialsExpired;

    private final boolean enabled;

    @Getter
    private final Map<String, Object> attributes;

    @Getter
    private final Collection<GrantedAuthority> authorities;

    public AuthenticatedPrincipal(UUID userId, String credentials, boolean expired, boolean locked, boolean credentialsExpired, boolean enabled,
                                  Map<String, Object> attributes, Collection<GrantedAuthority> authorities) {

        Assert.notNull(userId, "a userId must not be null");
        this.userId = userId;
        this.credentials = credentials;

        this.expired = expired;
        this.locked = locked;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;

        this.attributes = Collections.unmodifiableMap(Objects.requireNonNullElse(attributes, Collections.emptyMap()));
        this.authorities = Collections.unmodifiableCollection(Objects.requireNonNullElse(authorities, Collections.emptySet()));
    }

    public static AuthenticatedPrincipalBuilder builder() {
        return new AuthenticatedPrincipalBuilder();
    }

    @Override
    public String getName() {
        return this.userId.toString();
    }

    @Override
    public void eraseCredentials() {
        this.credentials = null;
    }

    @Override
    public String getPassword() {
        return this.credentials;
    }

    @Override
    public String getUsername() {
        return this.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    public static class AuthenticatedPrincipalBuilder {

        private UUID id;

        private String passwordHash;

        private boolean expired = false;
        private boolean locked = false;
        private boolean credentialsExpired = false;
        private boolean enabled = true;

        private final Map<String, Object> attributes = new HashMap<>();
        private final Collection<GrantedAuthority> authorities = new HashSet<>();

        private AuthenticatedPrincipalBuilder() {

        }

        public AuthenticatedPrincipalBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AuthenticatedPrincipalBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public AuthenticatedPrincipalBuilder expired(boolean expired) {
            this.expired = expired;
            return this;
        }

        public AuthenticatedPrincipalBuilder locked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public AuthenticatedPrincipalBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public AuthenticatedPrincipalBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AuthenticatedPrincipalBuilder attribute(String key, Object value) {
            this.attributes.put(key, value);
            return this;
        }

        public AuthenticatedPrincipalBuilder attributes(Consumer<Map<String, Object>> consumer) {
            consumer.accept(this.attributes);
            return this;
        }

        public AuthenticatedPrincipalBuilder authority(GrantedAuthority authority) {
            this.authorities.add(authority);
            return this;
        }

        public AuthenticatedPrincipalBuilder authorities(Consumer<Collection<GrantedAuthority>> consumer) {
            consumer.accept(this.authorities);
            return this;
        }

        public AuthenticatedPrincipal build() {
            return new AuthenticatedPrincipal(id, passwordHash, expired, locked, credentialsExpired, enabled, attributes, authorities);
        }
    }

}