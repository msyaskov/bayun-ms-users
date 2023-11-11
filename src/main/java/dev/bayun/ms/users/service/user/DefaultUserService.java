package dev.bayun.ms.users.service.user;

import dev.bayun.ms.users.entity.User;
import dev.bayun.ms.users.repository.UserRepository;
import dev.bayun.ms.users.util.RandomUserIdGenerator;
import dev.bayun.ms.users.util.UserIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Максим Яськов
 */
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Setter
    private UserIdGenerator userIdGenerator = new RandomUserIdGenerator();

    public User createFromCandidate(User candidate) {
        Assert.notNull(candidate, "A candidate must not be null");

        User user = User.builder()
                .id(this.userIdGenerator.generateUserId())
                .email(candidate.getEmail())
                .nickname(candidate.getNickname())
                .givenName(candidate.getGivenName())
                .familyName(candidate.getFamilyName())
                .picture(candidate.getPicture())
                .build();

        return this.userRepository.save(user);
    }

    public User findById(UUID userId) {
        Assert.notNull(userId, "A userId must not be null");
        return this.userRepository.findById(userId).orElseThrow(() -> this.userNotFoundByIdException(userId));
    }

    @Override
    public User findByEmail(String email) {
        Assert.notNull(email, "An email must not be null");
        return this.userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format("User with email=%s not found", email)));
    }

    public void deleteById(UUID userId) {
        this.userRepository.deleteById(userId);
    }

    public void update(User updated) {
        Assert.notNull(updated, "An user must not be null");
        if (!this.userRepository.existsById(updated.getId())) {
            throw this.userNotFoundByIdException(updated.getId());
        }

        this.userRepository.save(updated);
    }

    private RuntimeException userNotFoundByIdException(UUID userId) {
        return new UserNotFoundException(String.format("User with id=%s not found", userId));
    }

}
