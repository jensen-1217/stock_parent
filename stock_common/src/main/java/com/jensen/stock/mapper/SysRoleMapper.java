package com.jensen.stock.mapper;

import com.jensen.stock.pojo.entity.SysRole;

/**
* @author 59484
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:33
* @Entity com.jensen.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

}
