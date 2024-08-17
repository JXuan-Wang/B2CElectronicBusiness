package org.example.system.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.common.exception.MyException;
import org.example.system.manager.finalArgs.RedisKeyPrefix;
import org.example.system.manager.mapper.SysRoleUserMapper;
import org.example.system.manager.mapper.SysUserMapper;
import org.example.system.manager.service.SysUserService;
import org.example.system.model.dto.system.AssginRoleDto;
import org.example.system.model.dto.system.LoginDto;
import org.example.system.model.dto.system.SysUserDto;
import org.example.system.model.entity.system.SysUser;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.system.LoginVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    //用户退出
    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisKeyPrefix.USER_LOGIN+token);
    }

    //获取当前登录用户信息
    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get(RedisKeyPrefix.USER_LOGIN + token);
        return JSON.parseObject(userJson, SysUser.class);
    }

    //用户登录
    @Override
    public LoginVo login(LoginDto loginDto) {
        //获取输入验证码和存储到redis的key名称 LoginDto获取到
        String captcha = loginDto.getCaptcha();

        //根据获取的redis里面的key，查询redis里面存储验证码
        String codeKey = loginDto.getCodeKey();
        String redisCode = redisTemplate.opsForValue().get(RedisKeyPrefix.USER_VALIDATE + codeKey);

        //比较两个是否一致
        //如果不一致，提示用户，校验失败
        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode,captcha)){
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //如果一致，删除redis里面的验证码
        redisTemplate.delete(RedisKeyPrefix.USER_VALIDATE +codeKey);

        //获取提交用户名，loginDto获取到
        String username=loginDto.getUserName();

        //根据用户名查询数据库表sys_user表
        SysUser sysUser=sysUserMapper.selectUserInfoByUserName(username);

        //如果根据用户名查不到对应信息，用户不存在，返回错误信息
        if(sysUser==null){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        //如果根据用户名查询到用户信息，用户存在
        //获取输入的密码，比较输入的密码和数据库密码是否一致
        //把输入密码先进行加密再进行比较，md5
        String database_password=sysUser.getPassword();
        String input_password=DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        if(!input_password.equals(database_password)){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        //如果密码一致，登录成功，如果密码不一致登录失败
        //登录成功，生成用户唯一标识token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        //把登录成功用户信息放到redis里面
        //key : token value:用户信息
        redisTemplate.opsForValue().set(RedisKeyPrefix.USER_LOGIN +token,
                JSON.toJSONString(sysUser),
                7, TimeUnit.DAYS);

        //返回loginVo对象
        LoginVo loginVo=new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    //用户条件分页查询接口
    @Override
    public PageInfo<SysUser> findByPage(Integer pageNum,
                                        Integer pageSize,
                                        SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> list=sysUserMapper.findByPage(sysUserDto);
        return new PageInfo<>(list);
    }

    //用户添加
    @Override
    public void saveSysUser(SysUser sysUser) {
        //判度胺用户名不能重复
        String username=sysUser.getUserName();
        SysUser dbSysUser = sysUserMapper.selectUserInfoByUserName(username);
        if(dbSysUser!=null){
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        //输入密码进行加密
        String password=sysUser.getPassword();
        sysUser.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        sysUserMapper.save(sysUser);
    }

    //用户修改
    @Override
    public void updateSysUser(SysUser sysUser) {
        //判度胺用户名不能重复
        String username=sysUser.getUserName();
        SysUser dbSysUser = sysUserMapper.selectUserInfoByUserName(username);
        if(dbSysUser!=null){
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        sysUserMapper.update(sysUser);
    }

    //用户删除
    @Override
    public void deleteById(Integer userId) {
        sysUserMapper.delete(userId);
    }

    //用户分配角色
    @Override
    public void doAssign(AssginRoleDto assginRoleDto) {
        //根据userId删除用户之前分配角色数据
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());

        //重新分配新数据
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        //遍历得到每个角色id
        for (Long roleId : roleIdList) {
            sysRoleUserMapper.doAssign(assginRoleDto.getUserId(),roleId);
        }
    }
}
