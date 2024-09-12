package com.jensen.stock.mapper;

import com.jensen.stock.pojo.domain.UserPageListInfoDomain;
import com.jensen.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 59484
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:33
* @Entity com.jensen.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findUserInfoByUserName(@Param("userName") String userName);

    List<UserPageListInfoDomain> findUserAllInfoByPage(@Param("userName") String username, @Param("nickName") String nickName, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
