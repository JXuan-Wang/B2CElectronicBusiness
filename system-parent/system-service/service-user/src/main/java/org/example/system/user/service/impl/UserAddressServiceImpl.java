package org.example.system.user.service.impl;

import jakarta.annotation.Resource;
import org.example.system.model.entity.user.UserAddress;
import org.example.system.user.mapper.UserAddressMapper;
import org.example.system.user.service.UserAddressService;
import org.example.system.utils.AuthContextUtil;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Resource
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> findUserAddressList() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        return userAddressMapper.findByUserId(userId);
    }

    @Override
    public UserAddress getById(Long id) {
        return userAddressMapper.getById(id);
    }
}
