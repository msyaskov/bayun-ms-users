package dev.bayun.ms.users.service.user;

import dev.bayun.ms.users.entity.User;

import java.util.UUID;

/**
 * @author Максим Яськов
 */
public interface UserService {

    User createFromCandidate(User candidate);

    User findById(UUID userId);

    User findByEmail(String email);

    void deleteById(UUID userId);

    void patchById(UUID userId, User updated);

    boolean existsByNickname(String nickname);
}
