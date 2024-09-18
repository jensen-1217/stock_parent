package com.jensen.stock.security.service;

import com.google.common.base.Strings;
import com.jensen.stock.mapper.SysRoleMapper;
import com.jensen.stock.mapper.SysUserMapper;
import com.jensen.stock.pojo.entity.SysPermission;
import com.jensen.stock.pojo.entity.SysRole;
import com.jensen.stock.pojo.entity.SysUser;
import com.jensen.stock.security.user.LoginUserDetail;
import com.jensen.stock.service.PermissionService;
import com.jensen.stock.vo.resp.LoginRespPermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daocaoaren
 * @date 2024/7/23 12:57
 * @description : 定义获取用户详情服务bean
 */
@Service("userDetailsService")
public class LoginUserDetailService implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    /**
     * 根据传入的用户名称获取用户相关的详情信息：用户名，密文密码，权限集合等。。。
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //2.根据用户名查询用户信息
        SysUser dbUser = sysUserMapper.findUserInfoByUserName(username);
        //3.判断查询的用户信息
        if (dbUser==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        //4.2 成功则返回用户的正常信息
        //获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
        List<SysPermission> permissions = permissionService.findPermissionsByUserId(dbUser.getId());
        //获取树状权限菜单菜单数据，调用permissionService方法
        List<LoginRespPermission> menus = permissionService.getTree(permissions,0l,true);
        //获取菜单按钮集合
        List<String> authBtnPerms = permissions.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        //5.组装后端需要的权限标识
        //5.1 获取用户拥有的角色
        List<SysRole> roles = sysRoleMapper.getRoleByUserId(dbUser.getId());
        //5.2 将用户的权限标识和角色标识维护到权限集合中
//        List<String> pers = permissions.stream()
//                .filter(per -> StringUtils.isNotBlank(per.getPerms()))
//                .map(per -> per.getPerms())
//                .collect(Collectors.toList());
        List<String> ps=new ArrayList<>();
        permissions.stream().forEach(per->{
            if (StringUtils.isNotBlank(per.getPerms())) {
                ps.add(per.getPerms());
            }
        });
        roles.stream().forEach(role->{
            ps.add("ROLE_"+role.getName());
        });
        String[] psArray = ps.toArray(new String[ps.size()]);
        //5.3 将用户权限标识转化成权限对象集合
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(psArray);
        //6.封装用户详情信息实体对象
        LoginUserDetail loginUserDetail = new LoginUserDetail();
        //将用户的id nickname等相同属性信息复制到详情对象中
        BeanUtils.copyProperties(dbUser,loginUserDetail);
        loginUserDetail.setMenus(menus);
        loginUserDetail.setAuthorities(authorityList);
        loginUserDetail.setPermissions(authBtnPerms);
        return loginUserDetail;
    }
}
