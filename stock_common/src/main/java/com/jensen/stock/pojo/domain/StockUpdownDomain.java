package com.jensen.stock.pojo.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author by itheima
 * @Date 2022/2/28
 * @Description 股票涨跌信息
 */
@Schema(description = "股票涨跌信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StockUpdownDomain {
    /**
     * 股票编码
     */
    @Schema(description = "股票编码")
    @ExcelProperty(value = {"股票涨幅信息统计表","股票编码"},index = 0)
    private String code;
    /**
     * 股票名称
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","股票名称"},index = 1)
    @Schema(description = "股票名称")
    private String name;
    /**
     * 前收盘价
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","前收盘价格"},index = 2)
    @Schema(description = "前收盘价")
    private BigDecimal preClosePrice;
    /**
     * 当前价格
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","当前价格"},index= 3)
    @Schema(description = "当前价格")
    private BigDecimal tradePrice;
    /**
     * 涨跌
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","涨跌"},index= 4)
    @Schema(description = "涨跌")
    private BigDecimal increase;
    /**
     * 涨幅
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","涨幅"},index= 5)
    @Schema(description = "涨幅")
    private BigDecimal upDown;
    /**
     * 振幅
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","振幅"},index= 6)
    @Schema(description = "振幅")
    private BigDecimal amplitude;
    /**
     * 交易量
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","交易总量"},index = 7)
    @Schema(description = "交易量")
    private Long tradeAmt;
    /**
     * 交易金额
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","交易总金额"},index = 8)
    @Schema(description = "交易金额")
    private BigDecimal tradeVol;

    /**
     * 日期
     */
    @ExcelProperty(value = {"股票涨幅信息统计表","日期"},index = 9)
    @DateTimeFormat("yyy-MM-dd HH:mm")//easyExcel的注解-》excel
    @Schema(description = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curDate;
}