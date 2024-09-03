package com.jensen.stock.controller;

import com.jensen.stock.pojo.entity.SysUser;
import com.jensen.stock.service.UserService;
import com.jensen.stock.vo.req.LoginReqVo;
import com.jensen.stock.vo.resp.LoginRespVo;
import com.jensen.stock.vo.resp.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author jensen
 * @date 2024-08-24 12:18
 */
@RestController
@RequestMapping("/api")
@Tag(name = "用户处理相关接口处理器")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "根据用户名查询用户信息",description = "查询用户信息")
    @Parameters({
            @Parameter(name = "userName",description = "用户名",required = true)
    })
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String name){
        return userService.findByUserName(name);
    }

    @Operation(summary = "登录",description = "用户登录")
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);
    }

    @Operation(summary = "获取验证码",description = "获取验证码")
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }
}
