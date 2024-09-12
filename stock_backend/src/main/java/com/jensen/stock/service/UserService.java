package com.jensen.stock.service;

import com.jensen.stock.pojo.entity.SysUser;
import com.jensen.stock.vo.req.LoginReqVo;
import com.jensen.stock.vo.req.UserPageReqVo;
import com.jensen.stock.vo.resp.LoginRespVo;
import com.jensen.stock.vo.resp.PageResult;
import com.jensen.stock.vo.resp.R;

import java.util.Map;

public interface UserService {
    SysUser findByUserName(String userName);

    R<LoginRespVo> login(LoginReqVo vo);

    R<Map> getCaptchaCode();
    /**
     * 获取用户信息分页查询条件包含：分页信息 用户创建日期范围
     * @param userPageReqVo
     * @return
     */
    R<PageResult> getUserListPage(UserPageReqVo userPageReqVo);
}
