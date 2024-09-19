package com.jensen.stock.face.impl;

import com.jensen.stock.constant.StockConstant;
import com.jensen.stock.face.StockCacheFace;
import com.jensen.stock.mapper.StockBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jensen
 * @date 2024-09-19 18:13
 * @description
 */
@Component
@CacheConfig(cacheNames = StockConstant.STOCK)
public class StockCacheFaceImpl implements StockCacheFace {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;


    @Cacheable(key = "'stockCodes'")
    @Override
    public List<String> getAllStockCodeWithPredix() {
        //1.获取所有A股股票的编码
        List<String> allCodes = stockBusinessMapper.getStockIds();
        //2.添加股票前缀 sh sz
        List<String> prefixCodes = allCodes.stream().map(code -> {
            code = code.startsWith("6") ? "sh" + code : "sz" + code;
            return code;
        }).collect(Collectors.toList());
        return prefixCodes;
    }
}
