package org.example.system.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.user.UserAddress;

import java.util.List;

@Mapper
public interface UserAddressMapper {
    List<UserAddress> findByUserId(Long userId);

    UserAddress getById(Long id);
}
