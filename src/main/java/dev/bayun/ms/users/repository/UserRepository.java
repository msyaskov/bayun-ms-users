package dev.bayun.ms.users.repository;

import dev.bayun.ms.users.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Максим Яськов
 */
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

}
