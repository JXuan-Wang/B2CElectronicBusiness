package org.example.system.manager.service.impl;

import jakarta.annotation.Resource;
import org.example.system.common.log.service.AsyncOperLogService;
import org.example.system.manager.mapper.SysOperLogMapper;
import org.example.system.model.entity.system.SysOperLog;
import org.springframework.stereotype.Service;

@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {
    @Resource
    private SysOperLogMapper operLogMapper;

    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        operLogMapper.insert(sysOperLog);
    }
}
