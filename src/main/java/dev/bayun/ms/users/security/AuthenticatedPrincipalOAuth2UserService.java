package dev.bayun.ms.users.security;

import dev.bayun.sdk.security.AuthenticatedPrincipal;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Максим Яськов
 */
//@Service
public class AuthenticatedPrincipalOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2User defaultUser = (DefaultOAuth2User) super.loadUser(userRequest);
        Map<String, String> principal = defaultUser.getAttribute("principal");
        if (principal == null) {
            throw new IllegalStateException("a principal not found");
        }
        return AuthenticatedPrincipal.builder()
                .id(UUID.fromString(Objects.requireNonNull(principal.get("id"))))
                .authorities(authorities -> authorities.addAll(defaultUser.getAuthorities()))
                .attributes(attributes -> attributes.putAll(defaultUser.getAttributes()))
                .build();
    }

}
