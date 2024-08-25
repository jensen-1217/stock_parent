package com.jensen.stock.service.impl;

import com.jensen.stock.mapper.SysUserMapper;
import com.jensen.stock.pojo.entity.SysUser;
import com.jensen.stock.service.UserService;
import com.jensen.stock.vo.req.LoginReqVo;
import com.jensen.stock.vo.resp.LoginRespVo;
import com.jensen.stock.vo.resp.R;
import com.jensen.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser findByUserName(String userName) {
        return sysUserMapper.findUserInfoByUserName(userName);
    }

    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        if (vo==null|| StringUtils.isBlank(vo.getUsername())||StringUtils.isBlank(vo.getPassword())||StringUtils.isBlank(vo.getCode())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        SysUser dbUser = sysUserMapper.findUserInfoByUserName(vo.getUsername());
        if (dbUser==null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        if (!passwordEncoder.matches(vo.getPassword(), dbUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }

        LoginRespVo loginRespVo = new LoginRespVo();
        BeanUtils.copyProperties(dbUser,loginRespVo);
        return R.ok(loginRespVo);

    }
}
