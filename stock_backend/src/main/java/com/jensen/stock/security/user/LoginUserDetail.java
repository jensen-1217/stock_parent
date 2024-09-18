package com.jensen.stock.security.user;

import com.jensen.stock.vo.resp.LoginRespPermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/23 13:38
 * @description : 定义用户详情对象类
 */
@Schema(description = "其他自定义字段")
@Data
public class LoginUserDetail implements UserDetails {

    @Schema(hidden = true)
    private String username;
//    @Override
//    public String getUsername() {
//        return null;
//    }

    @Schema(hidden = true)
    private String password;
//    @Override
//    public String getPassword() {
//        return null;
//    }


    @Schema(hidden = true)
    private List<GrantedAuthority> authorities;
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }

    /**
     * true：账户没有过期
     */
    @Schema(description = "true：账户没有过期")
    private boolean isAccountNonExpired=true;
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }


    /**
     * true:账户没有锁定
     */
    @Schema(description = "true:账户没有锁定")
    private boolean isAccountNonLocked=true;
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }

    /**
     * true:密码没有过期
     */
    @Schema(description = "true:密码没有过期")
    private boolean isCredentialsNonExpired=true;
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }

    /**
     * true:账户可用
     */
    @Schema(description = "true:账户可用")
    private boolean isEnabled=true;
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }

    //其他自定义字段
    /**
     * 用户ID
     * 将Long类型数字进行json格式转化时，转成String格式类型
     */
   // @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户ID")
    private Long id;
    /**
     * 电话
     */

    @Schema(description = "电话")
    private String phone;


    /**
     * 昵称
     */

    @Schema(description = "昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    private String realName;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private Integer sex;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 邮件
     */
    @Schema(description = "邮件")
    private String email;

    /**
     * 侧边栏权限树（不包含按钮权限）
     */
    @Schema(description = "侧边栏权限树（不包含按钮权限）")
    private List<LoginRespPermission> menus;

    /**
     * 按钮权限标识
     */
    @Schema(description = "按钮权限标识")
    private List<String> permissions;

}
