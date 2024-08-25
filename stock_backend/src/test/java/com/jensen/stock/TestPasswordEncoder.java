package com.jensen.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author jensen
 * @date 2024-08-25 22:38
 * @description:
 */
@SpringBootTest
public class TestPasswordEncoder {
    @Autowired
    public PasswordEncoder passwordEncoder;

    @Test
    public void testPwd(){
        String pwd="123456";
        String encode = passwordEncoder.encode(pwd);
        System.out.println(encode);
        /*
            matches()匹配明文密码和加密后密码是否匹配，如果匹配，返回true，否则false
            just test
         */
        boolean flag = passwordEncoder.matches(pwd,"$2a$10$DKmZDcLlxnOY3tERzd5I4u07tdaIif.ZAKI7Z6uiG2lJYtfuFaOkW");
        System.out.println(flag);
    }

}
