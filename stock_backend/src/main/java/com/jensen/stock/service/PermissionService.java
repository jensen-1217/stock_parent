package com.jensen.stock.service;


import com.jensen.stock.pojo.entity.SysPermission;
import com.jensen.stock.vo.resp.LoginRespPermission;
import com.jensen.stock.vo.resp.PermissionTreeNodeVo;
import com.jensen.stock.vo.resp.R;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/21 23:48
 * @description :
 */
public interface PermissionService {

    /**
     * 根据用户id查询用户的所有权限
     * @param id 用户id
     * @return
     */
    List<SysPermission> findPermissionsByUserId(Long id);


    /**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    List<LoginRespPermission> getTree(List<SysPermission> permissions, long pid, boolean isOnlyMenuType);

}
