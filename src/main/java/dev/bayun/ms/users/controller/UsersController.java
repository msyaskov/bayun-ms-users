package dev.bayun.ms.users.controller;

import dev.bayun.ms.users.entity.User;
import dev.bayun.ms.users.rest.StringId;
import dev.bayun.ms.users.service.user.UserService;
import dev.bayun.ms.users.validation.group.OnCreate;
import dev.bayun.ms.users.validation.group.OnUpdate;
import dev.bayun.starter.restobject.RestDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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
    public RestDocument postUserFromCandidate(@RequestBody @Validated(OnCreate.class) User candidate) {
        User user = userService.createFromCandidate(candidate);
        return RestDocument.builder()
                .object("userId", StringId.builder()
                        .id(user.getId().toString())
                        .build())
                .build();
    }

    @GetMapping(path = "/{userId}")
    public RestDocument getUserById(@PathVariable("userId") UUID userId) {
        User user = userService.findById(userId);
        return RestDocument.builder()
                .object("user", user)
                .build();
    }

    @RequestMapping(method = RequestMethod.HEAD, params = "nickname")
    public ResponseEntity<?> headUserByNickname(@RequestParam String nickname) {
        boolean isExists = userService.existsByNickname(nickname);
        return (isExists ? ResponseEntity.ok() : ResponseEntity.notFound()).build();
    }

    @GetMapping(path = "/me")
    public RestDocument getUserMe(Authentication authentication) {
        User user = userService.findById(UUID.fromString(authentication.getName()));
        return RestDocument.builder()
                .object("user", user)
                .build();
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUserById(@PathVariable("userId") UUID userId) {
        userService.deleteById(userId);
    }

    @PatchMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void patchUserById(@PathVariable UUID userId, @RequestBody @Validated(OnUpdate.class) User patch) {
        userService.patchById(userId, patch);
    }
}
