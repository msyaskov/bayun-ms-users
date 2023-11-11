package dev.bayun.ms.users.rest;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.bayun.ms.users.entity.User;
import dev.bayun.sdk.rest.core.RestObject;

/**
 * @author Максим Яськов
 */
@JsonTypeName("user")
public final class UserRestObject extends RestObject<User, Void> {

    public UserRestObject() {
        super();
    }

    public UserRestObject(User object) {
        super(object, null);
    }
}
