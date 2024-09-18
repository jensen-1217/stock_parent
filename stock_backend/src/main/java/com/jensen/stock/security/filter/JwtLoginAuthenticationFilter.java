package com.jensen.stock.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jensen.stock.constant.StockConstant;
import com.jensen.stock.security.user.LoginUserDetail;
import com.jensen.stock.security.utils.JwtTokenUtil;
import com.jensen.stock.vo.req.LoginReqVo;
import com.jensen.stock.vo.resp.LoginRespVo;
import com.jensen.stock.vo.resp.R;
import com.jensen.stock.vo.resp.ResponseCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author by itheima
 * @Date 2022/1/21
 * @Description
 */
public class JwtLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private RedisTemplate redisTemplate;

    /**
     * 设置构造，传入自定义登录url地址
     * @param loginUrl
     */
    public JwtLoginAuthenticationFilter(String loginUrl) {
        super(loginUrl);
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //判断请求方法必须是post提交，且提交的数据的内容必须是application/json格式的数据
        if (!request.getMethod().equalsIgnoreCase("POST") ||
                ! (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE))) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //获取请求参数
        //获取reqeust请求对象的发送过来的数据流
        ServletInputStream in = request.getInputStream();
        //将数据流中的数据反序列化成Map
        LoginReqVo reqVo = new ObjectMapper().readValue(in, LoginReqVo.class);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        //校验输出的验证码是否正确
        //1.从redis中获取验证码
        String  redisCode =(String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + reqVo.getSessionId());
        if (StringUtils.isBlank(redisCode)) {
            R<Object> error= R.error(ResponseCode.CHECK_CODE_TIMEOUT);
            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
            return null;
        }
        //判断验证码是否正确
        if (!redisCode.equalsIgnoreCase(reqVo.getCode())) {
            R<Object> error=R.error(ResponseCode.CHECK_CODE_ERROR);
            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
            return null;
        }
        String username = reqVo.getUsername();
        username = (username != null) ? username : "";
        username = username.trim();
        String password = reqVo.getPassword();
        password = (password != null) ? password : "";
        //将用户名和密码信息封装到认证票据对象下
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        // Allow subclasses to set the "details" property
		//setDetails(request, authRequest);
        //调用认证管理器认证指定的票据对象
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 认证成功处理方法
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        LoginUserDetail principal = (LoginUserDetail) authResult.getPrincipal();
        String username = principal.getUsername();
        Collection<GrantedAuthority> authorities = principal.getAuthorities();
        //生成jwt
        String tokenStr = JwtTokenUtil.createToken(username, authorities.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        LoginRespVo respVo = new LoginRespVo();
        respVo.setAccessToken(tokenStr);
        BeanUtils.copyProperties(principal,respVo);
        R<LoginRespVo> ok = R.ok(respVo);

        response.getWriter().write(new ObjectMapper().writeValueAsString(ok));
    }

    /**
     * 认证失败处理方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        R<Object> error = R.error(ResponseCode.ERROR);

        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}