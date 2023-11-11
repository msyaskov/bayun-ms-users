package dev.bayun.ms.users.util;

import java.util.UUID;

/**
 * @author Максим Яськов
 */
public class RandomUserIdGenerator implements UserIdGenerator {

    @Override
    public UUID generateUserId() {
        return UUID.randomUUID();
    }

}
