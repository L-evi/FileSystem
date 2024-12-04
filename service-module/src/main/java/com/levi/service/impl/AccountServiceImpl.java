package com.levi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.SM3;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.levi.exception.AccountException;
import com.levi.exception.DataException;
import com.levi.exception.ParamException;
import com.levi.mapper.AccountMapper;
import com.levi.model.entity.AccountEntity;
import com.levi.model.request.AccountRequest;
import com.levi.model.view.AccountView;
import com.levi.service.AccountService;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AccountServiceImpl extends MPJBaseServiceImpl<AccountMapper, AccountEntity> implements AccountService {

    @Resource
    private Converter converter;

    @Override
    public AccountView login(AccountRequest accountRequest) {
        if (Objects.isNull(accountRequest) || Objects.isNull(accountRequest.getLoginType())) {
            throw ParamException.paramMissError("登录参数缺失");
        }
        if (StrUtil.isBlank(accountRequest.getRoleType())) {
            accountRequest.setRoleType("普通用户");
        }
        SM3 sm3 = SM3.create();
        switch (accountRequest.getLoginType()) {
            case "custom" -> {
                if (StrUtil.isBlank(accountRequest.getUsername()) || StrUtil.isBlank(accountRequest.getPassword())) {
                    throw ParamException.paramMissError("登录账号或密码缺失");
                }
                List<AccountEntity> accountEntities = baseMapper.selectList(Wrappers.<AccountEntity>lambdaQuery()
                        .eq(AccountEntity::getUsername, accountRequest.getUsername())
                        .eq(AccountEntity::getRoleType, accountRequest.getRoleType()));
                if (CollUtil.isEmpty(accountEntities) || CollUtil.size(accountEntities) != 1) {
                    throw AccountException.accountNotFound();
                }
                AccountEntity accountEntity = accountEntities.getFirst();
                if (!StrUtil.equals(accountEntity.getPassword(), sm3.digestHex(accountRequest.getPassword()))) {
                    throw AccountException.passwordError();
                }
                StpUtil.login(accountEntity.getUserId());
                AccountView accountView = converter.convert(accountEntity, AccountView.class);
                return accountView.setToken(StpUtil.getTokenValue());
            }
            case "wechat" -> {

            }
            default -> {
                break;
            }
        }
        return null;
    }

    @Override
    public void logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
    }

    @Override
    public AccountView register(AccountRequest accountRequest) {
        if (Objects.isNull(accountRequest)) {
            throw ParamException.paramMissError("注册参数缺失");
        }
        long userId = IdUtil.getSnowflakeNextId();
        AccountEntity accountEntity = converter.convert(accountRequest, AccountEntity.class);
        accountEntity.setUserId(userId);
        accountEntity.setStatus("启用");
        if (StrUtil.isBlank(accountEntity.getRoleType())) {
            accountEntity.setRoleType("普通用户");
        }
        switch (accountRequest.getLoginType()) {
            case "custom" -> {
                if (StrUtil.isBlank(accountRequest.getUsername()) || StrUtil.isBlank(accountRequest.getPassword())) {
                    throw ParamException.paramMissError("账号或密码缺失");
                }
                // 检查账号是否存在
                if (CollUtil.isNotEmpty(baseMapper.selectList(Wrappers.<AccountEntity>lambdaQuery().eq(AccountEntity::getUsername, accountRequest.getUsername())))) {
                    throw ParamException.paramError("账号已存在");
                }
                accountEntity.setPassword(SM3.create().digestHex(accountRequest.getPassword()));
            }
            case "wechat" -> {

            }
            default -> {
                break;
            }
        }
        int count = baseMapper.insert(accountEntity);
        if (count <= 0) {
            throw DataException.dataInsertError();
        }
        return getAccountInfo(userId);
    }

    @Override
    public AccountView getAccountInfo(Long userId) {
        if (Objects.isNull(userId)) {
            userId = StpUtil.getLoginIdAsLong();
        }
        AccountEntity accountEntity = baseMapper.selectById(userId);
        if (Objects.isNull(accountEntity)) {
            throw DataException.dataNotFoundError("该用户数据未找到");
        }
        return converter.convert(accountEntity, AccountView.class);
    }
}
