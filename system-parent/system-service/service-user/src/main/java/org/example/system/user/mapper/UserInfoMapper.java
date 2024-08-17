package org.example.system.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.user.UserInfo;

@Mapper
public interface UserInfoMapper {
    UserInfo selectByUsername(String username);

    void save(UserInfo userInfo);
}
