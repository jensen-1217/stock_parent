package com.jensen.stock.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jensen.stock.security.utils.JwtTokenUtil;
import com.jensen.stock.vo.resp.R;
import com.jensen.stock.vo.resp.ResponseCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author jensen
 * @date 2024-09-19 1:43
 * @description
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.获取http请求头中携带的jwt票据字符串（注意：如果用户尚未认证，则jwt票据字符串不存在）
        String jwtToken = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        //2.判断请求中的票据是否存在
        if (StringUtils.isBlank(jwtToken)) {
            //如果票据为空，可能用户准备取认证，所以不做拦截，但是此时UsernamePasswordAuthenticationToken对象未生成，那么即使放行本次请求
            //后续的过滤器链中也会校验认证票据对象
            filterChain.doFilter(request,response);
            return;
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        //3.校验票据
        Claims claims = JwtTokenUtil.checkJWT(jwtToken);
        //票据失效
        if (claims==null) {
            //票据失效则提示前端票据已经失效，需要重新认证
            R<Object> error = R.error(ResponseCode.INVALID_TOKEN);
            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
            return;
        }
        //4.从合法的票据中获取用户名和权限信息
        //用户名
        String username = JwtTokenUtil.getUsername(jwtToken);
        //权限信息 [P5, ROLE_ADMIN]
        String roles = JwtTokenUtil.getUserRole(jwtToken);
        //将数组格式的字符串转化成权限对象集合
        String comStr = StringUtils.strip(roles, "[]");
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(comStr);
        //5.组装认证成功的票据对象（认证成功时，密码位置null）
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorityList);
        //6.将认证成功的票据对象保存到安全上下文中，方便后续的过滤器直接获取权限信息
        SecurityContextHolder.getContext().setAuthentication(token);
        //7.发行过滤器
        filterChain.doFilter(request,response);
    }
}
