package com.jensen.stock;

import com.jensen.stock.mapper.StockBlockRtInfoMapper;
import com.jensen.stock.mapper.SysUserMapper;
import com.jensen.stock.pojo.domain.StockBlockDomain;
import com.jensen.stock.pojo.entity.SysUser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class TestSharding {
    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 测试默认数据源
     */
    @Test
    public void testDefaultDs(){
        SysUser user = sysUserMapper.selectByPrimaryKey(1237365636208922624l);
        System.out.println(user);
    }

}