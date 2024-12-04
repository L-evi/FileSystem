package com.levi.model.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AccountView {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "token")
    private String token;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "角色类型")
    private String roleType;

    @Schema(description = "用户头像地址")
    private String avatarImagePath;

}