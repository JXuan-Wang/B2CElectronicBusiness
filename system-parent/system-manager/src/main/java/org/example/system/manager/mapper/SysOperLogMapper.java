package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.system.SysOperLog;

@Mapper
public interface SysOperLogMapper {
    void insert(SysOperLog sysOperLog);
}
