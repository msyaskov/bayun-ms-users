package dev.bayun.ms.users.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.bayun.ms.users.validation.group.OnCreate;
import dev.bayun.ms.users.validation.group.OnUpdate;
import dev.bayun.starter.restobject.RestObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Максим Яськов
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonTypeName("user")
public class User implements RestObject {

    @Id
    private UUID id;

    @NotNull(groups = OnCreate.class)
    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotNull(groups = OnCreate.class)
    @Pattern(regexp = "^(?!.*_{2}.*)\\w{1,30}$", groups = {OnCreate.class, OnUpdate.class})
    private String nickname;

    @NotNull(groups = OnCreate.class)
    @Pattern(regexp = "^(?=.{1,30}$)(?!.*[ \\-.',]{2})[a-zа-яёЁA-ZА-Я]+([a-zа-яёЁA-ZА-Я \\-.',]*[a-zа-яёЁA-ZА-Я]+)*$", groups = {OnCreate.class, OnUpdate.class})
    private String givenName;

    @NotNull(groups = OnCreate.class)
    @Pattern(regexp = "^(?=.{1,30}$)(?!.*[ \\-.',]{2})[a-zа-яёЁA-ZА-Я]+([a-zа-яёЁA-ZА-Я \\-.',]*[a-zа-яёЁA-ZА-Я]+)*$", groups = {OnCreate.class, OnUpdate.class})
    private String familyName;

    private String picture;

}
