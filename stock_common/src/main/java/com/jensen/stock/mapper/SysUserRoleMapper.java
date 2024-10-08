package com.jensen.stock.mapper;

import com.jensen.stock.pojo.entity.SysUserRole;

/**
* @author 59484
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:33
* @Entity com.jensen.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

}
