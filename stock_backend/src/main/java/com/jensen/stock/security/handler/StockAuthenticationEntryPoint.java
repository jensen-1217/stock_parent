package com.jensen.stock.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jensen.stock.vo.resp.R;
import com.jensen.stock.vo.resp.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


import java.io.IOException;
import java.util.HashMap;

/**
 * @author jensen
 * @date 2024-09-18 18:53
 * @description
 */
@Slf4j
public class StockAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("匿名用户访问资源拒绝，原因：{}",authException.getMessage());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        R<Object> error = R.error(ResponseCode.ANONMOUSE_NOT_PERMISSION);
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
