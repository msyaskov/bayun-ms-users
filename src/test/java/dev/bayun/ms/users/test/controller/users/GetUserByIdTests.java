package dev.bayun.ms.users.test.controller.users;

import dev.bayun.ms.users.entity.User;
import dev.bayun.ms.users.service.user.UserNotFoundException;
import dev.bayun.ms.users.service.user.UserService;
import dev.bayun.starter.restobject.RestDocument;
import dev.bayun.starter.restobject.RestObjectAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static dev.bayun.ms.users.test.TestResultMatchers.rest;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * GET /api/users/{userId}
 *
 * if successful -> 200 OK & User
 * if you unauthenticated -> 401 Unauthenticated
 * if a user not found -> 404 Not Found
 *
 * @author Максим Яськов
 */

@WebMvcTest
@ImportAutoConfiguration(RestObjectAutoConfiguration.class)
public class GetUserByIdTests {

    private static final String MOCK_USER_ID = "005afe26-24e5-4bcc-be49-84ab6e77032b";

    private static final String REQUEST_PATH = "/api/users/{userId}";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(MOCK_USER_ID)
    public void whenUserFound_expect200OkAndBody() throws Exception {
        User mockUser = User.builder()
                .id(UUID.fromString(MOCK_USER_ID))
                .nickname("nickname")
                .picture("picture")
                .email("example@mail.com")
                .familyName("Family")
                .givenName("Name")
                .build();

        // when
        when(userService.findById(UUID.fromString(MOCK_USER_ID))).thenReturn(mockUser);

        // then
        mvc.perform(MockMvcRequestBuilders.get(REQUEST_PATH, MOCK_USER_ID))
                .andExpect(status().isOk())
                .andExpect(rest().document(RestDocument.builder()
                        .object("user", mockUser)
                        .build()));
    }

    @Test
    public void whenUnauthenticated_expect401Unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REQUEST_PATH, MOCK_USER_ID))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(MOCK_USER_ID)
    public void whenUnknownUserID_expect404NotFound() throws Exception {
        when(userService.findById(UUID.fromString(MOCK_USER_ID))).thenThrow(new UserNotFoundException());

        mvc.perform(MockMvcRequestBuilders.get(REQUEST_PATH, MOCK_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
