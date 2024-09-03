package com.jensen.stock.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户表
 * @TableName sys_user
 */
@Data
@Schema(title = "用户基本信息",description = "SysUser")
public class SysUser implements Serializable {
    /**
     * 用户id
     */
    @Schema(title = "主键ID")
    private Long id;

    /**
     * 账户
     */
    @Schema(title = "账户")
    private String username;

    /**
     * 用户密码密文
     */
    @Schema(title = "用户密码密文")
    private String password;

    /**
     * 手机号码
     */
    @Schema(title = "手机号码")
    private String phone;

    /**
     * 真实名称
     */
    @Schema(title = "真实名称")
    private String realName;

    /**
     * 昵称
     */
    @Schema(title = "昵称")
    private String nickName;

    /**
     * 邮箱(唯一)
     */
    @Schema(title = "邮箱(唯一)")
    private String email;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    @Schema(title = "账户状态(1.正常 2.锁定 )")
    private Integer status;

    /**
     * 性别(1.男 2.女)
     */
    @Schema(title = "性别(1.男 2.女)")
    private Integer sex;

    /**
     * 是否删除(1未删除；0已删除)
     */
    @Schema(title = "是否删除(1未删除；0已删除)")
    private Integer deleted;

    /**
     * 创建人
     */
    @Schema(title = "创建人")
    private Long createId;

    /**
     * 更新人
     */
    @Schema(title = "更新人")
    private Long updateId;

    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    @Schema(title = "创建来源(1.web 2.android 3.ios )")
    private Integer createWhere;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private Date updateTime;

    @Schema(title = "sessionId")
    private static final long serialVersionUID = 1L;
}