package com.jensen.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jensen
 * @date 2024-09-10 15:34
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4WeekDomain {
    /**
     * 一周内平均价
     */
    private BigDecimal avgPrice;
    /**
     * 一周内最低价
     */
    private BigDecimal minPrice;
    /**
     * 周一开盘价
     */
    private BigDecimal openPrice;
    /**
     * 一周内最高价
     */
    private BigDecimal maxPrice;
    /**
     * 周五收盘价（如果当前日期不到周五，则显示最新价格）
     */
    private BigDecimal closePrice;
    /**
     * 一周内最大时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date mxTime;
    /**
     * 股票编码
     */
    private String stockCode;
    /**
     * 一周内最小时间
     * 临时字段，前端无需展示
     */
    @JsonIgnore
    private Date minTime;

}
