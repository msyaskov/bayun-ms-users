package dev.bayun.ms.users.service.user;

import dev.bayun.ms.users.entity.User;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Максим Яськов
 */
public interface UserService {

    User createFromCandidate(User candidate);

    User findById(UUID userId);

    User findByEmail(String email);

    void deleteById(UUID userId);

    void update(User updated);

}
