package dev.bayun.ms.users.rest;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * @author Максим Яськов
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserRestObject.class, name = "user"),
        @JsonSubTypes.Type(value = IdRestObject.class, name = "id")
})
public abstract class RestObjectMixin {
}
