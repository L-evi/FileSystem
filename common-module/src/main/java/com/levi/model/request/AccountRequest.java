package com.levi.model.request;

import com.levi.model.entity.AccountEntity;
import io.github.linpeilie.annotations.AutoMapper;
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
@AutoMapper(target = AccountEntity.class)
public class AccountRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "角色类型")
    private String roleType;

    @Schema(description = "登录类型")
    private String loginType;

    @Schema(description = "用户头像地址")
    private String avatarImagePath;
}