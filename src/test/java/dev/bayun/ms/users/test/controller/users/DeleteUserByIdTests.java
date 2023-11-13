package dev.bayun.ms.users.test.controller.users;

import dev.bayun.ms.users.configuration.WebSecurityConfiguration;
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
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
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
 * if successful -> 200 OK
 * if not found -> 200 OK
 * if you unauthenticated -> 401 Unauthenticated
 * if you don't have enough authority -> 403 Forbidden
 *
 * @author Максим Яськов
 */

@WebMvcTest
@ActiveProfiles("test")
@Import(WebSecurityConfiguration.class)
@ImportAutoConfiguration(RestObjectAutoConfiguration.class)
public class DeleteUserByIdTests {

    private static final String MOCK_USER_ID = "005afe26-24e5-4bcc-be49-84ab6e77032b";

    private static final String REQUEST_PATH = "/api/users/{userId}";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(authorities = "users.write")
    public void whenSuccess_expect200Ok() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REQUEST_PATH, MOCK_USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void whenUnauthenticated_expect401() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REQUEST_PATH, MOCK_USER_ID))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void whenNotEnoughAuthority_expect403() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REQUEST_PATH, MOCK_USER_ID))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));
    }

}
