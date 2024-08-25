package com.jensen.stock.mapper;

import com.jensen.stock.pojo.entity.SysLog;

/**
* @author 59484
* @description 针对表【sys_log(系统日志)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:32
* @Entity com.jensen.stock.pojo.entity.SysLog
*/
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

}
