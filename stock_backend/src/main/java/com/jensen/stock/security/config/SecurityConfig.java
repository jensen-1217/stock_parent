package com.jensen.stock.security.config;

import com.jensen.stock.security.filter.JwtAuthorizationFilter;
import com.jensen.stock.security.filter.JwtLoginAuthenticationFilter;
import com.jensen.stock.security.handler.StockAccessDeniedHandler;
import com.jensen.stock.security.handler.StockAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity//开启web安全设置生效
//开启SpringSecurity相关注解支持
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;
    /**
     * 定义公共的无需被拦截的资源
     * @return
     */
    private String[] getPubPath(){
        //公共访问资源
        String[] urls = {
                "/*/*.css", "/*/*.js", "/favicon.ico", "/doc.html",
                "/druid/*", "/webjars/*", "/v2/api-docs", "/api/captcha",
                "/swagger/*", "/swagger-resources/*", "/swagger-ui.html"
        };
        return urls;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        //登出功能
        http.logout().logoutUrl("/api/logout").invalidateHttpSession(true);
        //开启允许iframe 嵌套。security默认禁用ifram跨域与缓存
        http.headers().frameOptions().disable().cacheControl().disable();
        //session禁用
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();//禁用跨站请求伪造
        http.authorizeHttpRequests()//对资源进行认证处理
                .requestMatchers(getPubPath()).permitAll()//公共资源都允许访问
                .anyRequest().authenticated();  //除了上述资源外，其它资源，只有 认证通过后，才能有权访问
                //坑-过滤器要添加在默认过滤器之前，否则，登录失效
        http.addFilterBefore(jwtLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //配置授权过滤器，过滤一切资源
        http.addFilterBefore(jwtAuthorizationFilter(),JwtLoginAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(new StockAccessDeniedHandler())
        .authenticationEntryPoint(new StockAuthenticationEntryPoint());

        return http.build();
    }

    @Bean
    public JwtLoginAuthenticationFilter jwtLoginAuthenticationFilter() throws Exception {
        JwtLoginAuthenticationFilter authenticationFilter = new JwtLoginAuthenticationFilter("/api/login");
        authenticationFilter.setAuthenticationManager(authenticationManager());
     authenticationFilter.setRedisTemplate(redisTemplate);
     return authenticationFilter;
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager authenticationManager =
                authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }

    /**
     * 自定义授权过滤器
     * @return
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}