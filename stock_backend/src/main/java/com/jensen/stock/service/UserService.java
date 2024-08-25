package com.jensen.stock.service;

import com.jensen.stock.pojo.entity.SysUser;
import com.jensen.stock.vo.req.LoginReqVo;
import com.jensen.stock.vo.resp.LoginRespVo;
import com.jensen.stock.vo.resp.R;

public interface UserService {
    SysUser findByUserName(String userName);

    R<LoginRespVo> login(LoginReqVo vo);
}
