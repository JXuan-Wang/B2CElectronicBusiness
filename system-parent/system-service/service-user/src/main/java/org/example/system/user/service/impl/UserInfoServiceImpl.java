package org.example.system.user.service.impl;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.example.system.common.exception.MyException;
import org.example.system.model.dto.h5.UserLoginDto;
import org.example.system.model.dto.h5.UserRegisterDto;
import org.example.system.model.entity.user.UserInfo;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.h5.UserInfoVo;
import org.example.system.user.mapper.UserInfoMapper;
import org.example.system.user.service.UserInfoService;
import org.example.system.utils.AuthContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    //注册
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        // userRegisterDto获取数据
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();

        // 验证码校验
        // 从redis获取发送验证码
        // 获取输入的验证码，进行比对
        String redisCode = redisTemplate.opsForValue().get(username);
        if(!redisCode.equals(code)){
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        // 校验用户名不能重复
        UserInfo userInfo=userInfoMapper.selectByUsername(username);
        if(userInfo!=null){
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        // 封装添加数据，调用方法添加数据库
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        userInfoMapper.save(userInfo);

        // 从redis删除发送的验证码
    }

    @Override
    public Object login(UserLoginDto userLoginDto) {
        // dto获取用户名和密码
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        // 根据用户名查询数据库，得到用户信息
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if(userInfo==null){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 比较密码是否一致
        String databasePassword = userInfo.getPassword();
        String md5Passord = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!md5Passord.equals(databasePassword)){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 校验用户是否被禁用
        if(userInfo.getStatus()==0){
            throw new MyException(ResultCodeEnum.ACCOUNT_STOP);
        }

        // 生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        // 把用户信息放到redis里面
        redisTemplate.opsForValue().set("user:system"+token
                , JSON.toJSONString(userInfo)
                ,30, TimeUnit.DAYS);

        //返回token
        return token;
    }

    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        // 从redis根据token获取用户信息
//        String userJson = redisTemplate.opsForValue().get("user:system" + token);
//        if(StringUtils.hasText(userJson)||userJson==null){
//            throw new MyException(ResultCodeEnum.LOGIN_AUTH);
//        }
//        UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
        //从threadLocal获取用户信息
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        //userinfo --> userinfoVo
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);
        return userInfoVo;
    }
}
