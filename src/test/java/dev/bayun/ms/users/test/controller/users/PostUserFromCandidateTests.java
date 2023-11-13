package dev.bayun.ms.users.test.controller.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.ms.users.configuration.WebSecurityConfiguration;
import dev.bayun.ms.users.entity.User;
import dev.bayun.ms.users.rest.StringId;
import dev.bayun.ms.users.service.user.NicknameOccupiedException;
import dev.bayun.ms.users.service.user.UserService;
import dev.bayun.starter.restobject.RestDocument;
import dev.bayun.starter.restobject.RestObjectAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static dev.bayun.ms.users.test.TestResultMatchers.rest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * POST /api/users/me
 *
 * if successful -> 200 OK & UserId
 * if you unauthenticated -> 401 Unauthenticated
 * if you don't have enough authority -> 403 Forbidden
 * if a user with the same nickname already exists -> 409 Conflict
 * if an invalid candidate -> 400 Bad Request
 *
 * @author Максим Яськов
 */

@WebMvcTest
@ActiveProfiles("test")
@Import(WebSecurityConfiguration.class)
@ImportAutoConfiguration(RestObjectAutoConfiguration.class)
public class PostUserFromCandidateTests {

    private static final String MOCK_USER_ID = "005afe26-24e5-4bcc-be49-84ab6e77032b";

    private static final String REQUEST_PATH = "/api/users";

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(authorities = "users.write")
    public void whenSuccess_expected200AndUserId() throws Exception {
        User mockUser = User.builder()
                .id(UUID.fromString(MOCK_USER_ID)) // ?
                .nickname("nickname")
                .picture("picture")
                .email("example@mail.com")
                .familyName("Family")
                .givenName("Name")
                .build();

        // when
        when(userService.createFromCandidate(mockUser)).thenReturn(mockUser);

        // then
        mvc.perform(MockMvcRequestBuilders.post(REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isCreated())
                .andExpect(rest().document(RestDocument.builder()
                        .object("userId", new StringId(mockUser.getId().toString()))
                        .build()));
    }

    @Test
    @WithMockUser
    public void whenNotEnoughAuthority_expected403() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.builder().build())))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));
    }

    @Test
    public void whenUnauthenticated_expect401() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.builder().build())))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(authorities = "users.write")
    public void whenNicknameOccupied_expect409() throws Exception {
        User candidate = User.builder()
                .email("example@mail.com")
                .nickname("nickname")
                .givenName("name")
                .familyName("family")
                .build();

        when(userService.createFromCandidate(any())).thenThrow(new NicknameOccupiedException());

        mvc.perform(MockMvcRequestBuilders.post(REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidate)))
                .andExpect(status().isConflict())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(authorities = "users.write")
    public void whenUserInvalid_expect400() throws Exception {
        User candidate = User.builder().build();

        mvc.perform(MockMvcRequestBuilders.post(REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidate)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

}
