package com.jensen.stock.service.impl;

import com.google.common.collect.Lists;
import com.jensen.stock.mapper.SysPermissionMapper;
import com.jensen.stock.mapper.SysRolePermissionMapper;
import com.jensen.stock.pojo.entity.SysPermission;
import com.jensen.stock.service.PermissionService;
import com.jensen.stock.utils.IdWorker;
import com.jensen.stock.vo.resp.LoginRespPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/21 23:49
 * @description :
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;


    @Override
    public List<SysPermission> findPermissionsByUserId(Long id) {
        List<SysPermission> list = sysPermissionMapper.findPermissionsByUserId(id);
        return list;
    }

    /**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    @Override
    public List<LoginRespPermission> getTree(List<SysPermission> permissions, long pid, boolean isOnlyMenuType) {
        //创建一个集合，用于存放便利好的树状菜单
        ArrayList<LoginRespPermission> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(permissions)) {
            return list;
        }
        //便利查询到的所有用户权限
        for (SysPermission permission : permissions) {
            if (permission.getPid().equals(pid)) {
                //判断是否是按钮，如果左边是true，右边则不再运算直接返回true，只要有一个是真结果就是真
                //这里设置不能为3是因为这里3代表的是按钮，不是菜单栏里面的东西了所以就不需要将按钮添加到菜单栏当中，会额外的去获取按钮
                if (permission.getType().intValue()!=3 || !isOnlyMenuType) {
                    LoginRespPermission respNodeVo = new LoginRespPermission();
                    respNodeVo.setId(permission.getId());
                    respNodeVo.setTitle(permission.getTitle());
                    respNodeVo.setIcon(permission.getIcon());
                    respNodeVo.setPath(permission.getUrl());
                    respNodeVo.setName(permission.getName());
                    //通过递归的方式去找他的子集，这里也就是说将整个集合传递到里面去寻找，通过本次查找的id值去找，
                    //就比如说，当前的这个admin，如果是顶级目录，那么他就没有父级，所以他的pid就是0，如果是菜单，那么他的pid可能就是这个目录的id
                    //所以我们根据以目录的id，目录下面有很多菜单，如果归属与这个目录自然菜单的pid就等于目录的id，如果归属与这个菜单，那么按钮的pid就是菜单的id
                    //Children的类型是List<LoginRespPermission>，也就是这个方法返回的数据类型和这个属性的类型一致
                    respNodeVo.setChildren(getTree(permissions,permission.getId(),isOnlyMenuType));
                    list.add(respNodeVo);
                }
            }
        }
        return list;
    }
}
