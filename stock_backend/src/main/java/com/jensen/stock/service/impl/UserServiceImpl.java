package com.jensen.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import com.google.common.base.Strings;
import com.jensen.stock.constant.StockConstant;
import com.jensen.stock.mapper.SysUserMapper;
import com.jensen.stock.pojo.entity.SysPermission;
import com.jensen.stock.pojo.entity.SysUser;
import com.jensen.stock.service.PermissionService;
import com.jensen.stock.service.UserService;
import com.jensen.stock.utils.IdWorker;
import com.jensen.stock.vo.req.LoginReqVo;
import com.jensen.stock.vo.resp.LoginRespPermission;
import com.jensen.stock.vo.resp.LoginRespVo;
import com.jensen.stock.vo.resp.R;
import com.jensen.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PermissionService permissionService;

    @Override
    public SysUser findByUserName(String userName) {
        return sysUserMapper.findUserInfoByUserName(userName);
    }

    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //1.判断参数是否合法
        if (vo==null|| StringUtils.isBlank(vo.getUsername())||StringUtils.isBlank(vo.getPassword())||StringUtils.isBlank(vo.getCode())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //判断校验码和sessionId是有效
        if (StringUtils.isBlank(vo.getCode())||StringUtils.isBlank(vo.getSessionId())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //判断验证码是否正确
        //1.从redis中获取验证码
        String  redisCode =(String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + vo.getSessionId());
        if (StringUtils.isBlank(redisCode)) {
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        //判断验证码是否正确
        if (!redisCode.equalsIgnoreCase(vo.getCode())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //根据用户名去数据库中查询用户信息，获取密码的密文
        SysUser dbUser = sysUserMapper.findUserInfoByUserName(vo.getUsername());
        if (dbUser==null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //调用匹配器匹配前端传送来的密码和数据库中的密文是否对应
        if (!passwordEncoder.matches(vo.getPassword(), dbUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
//4.响应
        LoginRespVo loginRespVo = new LoginRespVo();
        BeanUtils.copyProperties(dbUser,loginRespVo);
        //获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
        List<SysPermission> permissions = permissionService.findPermissionsByUserId(dbUser.getId());
        //获取树状权限菜单菜单数据，调用permissionService方法
        List<LoginRespPermission> menus = permissionService.getTree(permissions,0l,true);
        //获取菜单按钮集合
        List<String> authBtnPerms = permissions.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        loginRespVo.setMenus(menus);
        loginRespVo.setPermissions(authBtnPerms);
        //生成token
        loginRespVo.setAccessToken(dbUser.getId()+":"+dbUser.getUsername());



        return R.ok(loginRespVo);

    }

    @Override
    public R<Map> getCaptchaCode() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        captcha.setBackground(Color.LIGHT_GRAY);
        String checkCode = captcha.getCode();
        String imageData = captcha.getImageBase64();
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的图片校验码：{}，会话id：{}",checkCode,sessionId);
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX+sessionId,checkCode,5, TimeUnit.MINUTES);
        HashMap<String, String> data = new HashMap<>();
        data.put("imageData",imageData);
        data.put("sessionId",sessionId);
        return R.ok(data);
    }
}
