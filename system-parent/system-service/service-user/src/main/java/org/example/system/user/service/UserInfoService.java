package org.example.system.user.service;

import org.example.system.model.dto.h5.UserLoginDto;
import org.example.system.model.dto.h5.UserRegisterDto;
import org.example.system.model.vo.h5.UserInfoVo;

public interface UserInfoService {
    void register(UserRegisterDto userRegisterDto);

    Object login(UserLoginDto userLoginDto);

    UserInfoVo getCurrentUserInfo(String token);
}
