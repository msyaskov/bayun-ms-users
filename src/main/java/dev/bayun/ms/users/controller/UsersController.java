package dev.bayun.ms.users.controller;

import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
import dev.bayun.ms.users.entity.User;
import dev.bayun.ms.users.rest.IdRestObject;
import dev.bayun.ms.users.rest.StringId;
import dev.bayun.ms.users.rest.UserRestObject;
import dev.bayun.ms.users.service.user.UserService;
import dev.bayun.sdk.rest.core.RestDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Максим Яськов
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestDocument postUserFromCandidate(@RequestBody User candidate) {
        User user = userService.createFromCandidate(candidate);
        return RestDocument.builder()
                .object("userId", new IdRestObject(new StringId(user.getId().toString())))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{userId}")
    public RestDocument getUserById(@PathVariable("userId") UUID userId) {
        User user = userService.findById(userId);
        return RestDocument.builder()
                .object("user", new UserRestObject(user))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/me")
    public RestDocument getUserMe(Authentication authentication) {
        User user = userService.findById(UUID.fromString(authentication.getName()));
        return RestDocument.builder()
                .object("user", new UserRestObject(user))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/id", params = "email")
    public RestDocument getUserById(@RequestParam("email") String email) {
        User user = userService.findByEmail(email);
        return RestDocument.builder()
                .object("userId", new IdRestObject(new StringId(user.getId().toString())))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{userId}")
    public void deleteUserById(@PathVariable("userId") UUID userId) {
        userService.deleteById(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserById(User updated) {
        userService.update(updated);
    }
}
