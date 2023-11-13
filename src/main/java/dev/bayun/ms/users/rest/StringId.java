package dev.bayun.ms.users.rest;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.bayun.starter.restobject.RestObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Максим Яськов
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("id")
public class StringId implements RestObject {

    private String id;

}
