package com.levi.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.levi.model.BaseEntity;
import com.levi.model.view.AccountView;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("t_account")
@AutoMapper(target = AccountView.class)
public class AccountEntity extends BaseEntity {

    @Schema(description = "用户id")
    @TableId
    private Long userId;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "角色类型")
    private String roleType;

    @Schema(description = "用户头像地址")
    private String avatarImagePath;

}
