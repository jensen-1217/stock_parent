package com.jensen.stock.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description
 */
@Schema(description = "")
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    //A股大盘ID集合
    @Schema(description = "A股大盘ID集合")
    private List<String> inner;
    //外盘ID集合
    @Schema(description = "外盘ID集合")
    private List<String> outer;
    //股票区间
    @Schema(description = "股票区间")
    private List<String> upDownRange;

    //大盘 外盘 个股的公共URL
    @Schema(description = "大盘 外盘 个股的公共URL")
    private String marketUrl;

    //板块采集的URL
    @Schema(description = "板块采集的URL")
    private String blockUrl;
}