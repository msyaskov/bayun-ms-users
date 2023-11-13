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

        if (userRepository.existsByNickname(candidate.getNickname())) {
            throw new NicknameOccupiedException();
        }

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

    public void patchById(UUID userId, User patch) {
        Assert.notNull(patch, "A patch must not be null");

        User user = this.findById(userId);

        if (patch.getNickname() != null) {
            if (this.userRepository.existsByNickname(patch.getNickname())) {
                throw new NicknameOccupiedException();
            } else {
                user.setEmail(patch.getEmail());
            }
        }

        if (patch.getGivenName() != null) {
            user.setGivenName(patch.getGivenName());
        }

        if (patch.getFamilyName() != null) {
            user.setFamilyName(patch.getFamilyName());
        }

        if (patch.getPicture() != null) {
            user.setPicture(patch.getPicture());
        }

        if (patch.getEmail() != null) {
            user.setEmail(patch.getEmail());
        }

        this.userRepository.save(patch);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        Assert.notNull(nickname, "A nickname must not be null");
        return userRepository.existsByNickname(nickname);
    }

    private RuntimeException userNotFoundByIdException(UUID userId) {
        return new UserNotFoundException(String.format("User with id=%s not found", userId));
    }

}
