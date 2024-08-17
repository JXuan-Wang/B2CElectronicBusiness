package org.example.system.common.log.service;

import org.example.system.model.entity.system.SysOperLog;

public interface AsyncOperLogService {
    public abstract void saveSysOperLog(SysOperLog sysOperLog) ;
}
