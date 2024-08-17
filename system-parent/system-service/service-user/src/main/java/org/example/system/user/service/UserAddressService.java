package org.example.system.user.service;

import org.example.system.model.entity.user.UserAddress;

import java.util.List;

public interface UserAddressService {
    List<UserAddress> findUserAddressList();

    UserAddress getById(Long id);
}
