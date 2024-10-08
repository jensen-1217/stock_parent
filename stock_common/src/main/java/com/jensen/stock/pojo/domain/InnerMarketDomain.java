package com.jensen.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jensen
 * @date 2024-08-26 21:58
 * @description 定义大盘数据的领域对象
 */
@Schema(description = "定义大盘数据的领域对象")
@Data
public class InnerMarketDomain {

    /**
     * 大盘编码
     */
    @Schema(title = "大盘编码")
    private String code;
    /**
     * 大盘名称
     */
    @Schema(title = "大盘名称")
    private String name;
    /**
     * 开盘点
     */
    @Schema(title = "开盘点")
    private BigDecimal openPoint;
    /**
     * 当前点
     */
    @Schema(title = "当前点")
    private BigDecimal curPoint;
    /**
     * 前收盘点
     */
    @Schema(title = "前收盘点")
    private BigDecimal preClosePoint;
    /**
     * 交易量
     */
    @Schema(title = "交易量")
    private Long tradeAmt;
    /**
     * 交易金额
     */
    @Schema(title = "交易金额")
    private Long tradeVol;
    /**
     * 涨跌值
     */
    @Schema(title = "涨跌值")
    private BigDecimal upDown;
    /**
     * 涨幅
     */
    @Schema(title = "涨幅")
    private BigDecimal rose;

    /**
     * 振幅
     */
    @Schema(title = "振幅")
    private BigDecimal amplitude;
    /**
     * 当前时间
     */
    @Schema(title = "当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curTime;
}
