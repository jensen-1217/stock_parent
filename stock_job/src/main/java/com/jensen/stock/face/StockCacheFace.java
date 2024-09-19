package com.jensen.stock.face;

import java.util.List;

/**
 * @author jensen
 * @date 2024-09-19 18:12
 * @description
 */
public interface StockCacheFace {
    /**
     * 获取所有股票编码，并添加上证或者深证的股票前缀编号：sh sz
     * @return
     */
    List<String> getAllStockCodeWithPredix();
}
