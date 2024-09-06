package com.jensen.stock;

import com.google.common.collect.Lists;
import com.jensen.stock.mapper.StockBusinessMapper;
import com.jensen.stock.service.StockTimerTaskService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jensen
 * @date 2024-09-06 9:19
 * @description
 */
@SpringBootTest
public class TestMapper {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @Test
    public void test01(){
        List<String> stockIds = stockBusinessMapper.getStockIds();
        System.out.println("stockIds = " + stockIds);
        stockIds=stockIds.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        System.out.println("stockIds = " + stockIds);
        Lists.partition(stockIds,15).forEach(ids-> {
            System.out.println(ids);
        });
    }

    @Test
    public void test02(){
        stockTimerTaskService.getStockRtIndex();
    }
}
