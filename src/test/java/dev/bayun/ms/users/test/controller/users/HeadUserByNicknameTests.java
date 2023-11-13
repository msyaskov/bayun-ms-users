package dev.bayun.ms.users.test.controller.users;

import dev.bayun.ms.users.service.user.UserService;
import dev.bayun.starter.restobject.RestObjectAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * GET /api/users/{userId}
 *
 * if successful -> 200 OK
 * if you unauthenticated -> 401 Unauthenticated
 * if a user not found -> 404 Not Found
 *
 * @author Максим Яськов
 */

@WebMvcTest
@ImportAutoConfiguration(RestObjectAutoConfiguration.class)
public class HeadUserByNicknameTests {

    private static final String MOCK_USER_ID = "005afe26-24e5-4bcc-be49-84ab6e77032b";

    private static final String REQUEST_PATH = "/api/users";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(MOCK_USER_ID)
    public void whenSuccess_expect200() throws Exception {
        // when
        when(userService.existsByNickname("nickname")).thenReturn(true);

        // then
        mvc.perform(MockMvcRequestBuilders.head(REQUEST_PATH)
                        .param("nickname", "nickname"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void whenUnauthenticated_expect401() throws Exception {
        mvc.perform(MockMvcRequestBuilders.head(REQUEST_PATH)
                        .param("nickname", "nickname"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(MOCK_USER_ID)
    public void whenNotFound_expect404() throws Exception {
        when(userService.existsByNickname("nickname")).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.head(REQUEST_PATH)
                        .param("nickname", "nickname"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
