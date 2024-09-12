package com.jensen.stock.mapper;

import com.jensen.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 59484
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:33
* @Entity com.jensen.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> findPermissionsByUserId(@Param("id") Long id);
}
