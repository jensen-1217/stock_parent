package com.jensen.stock.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jensen.stock.vo.resp.R;
import com.jensen.stock.vo.resp.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;


import java.io.IOException;
import java.util.HashMap;

/**
 * @author jensen
 * @date 2024-09-18 18:53
 * @description
 */
@Slf4j
public class StockAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("当前用户访问资源拒绝，原因：{}",accessDeniedException.getMessage());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        R<Object> error = R.error(ResponseCode.NOT_PERMISSION);
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
