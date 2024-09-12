package com.jensen.stock.controller;

import com.jensen.stock.pojo.domain.*;
import com.jensen.stock.service.StockService;
import com.jensen.stock.vo.resp.PageResult;
import com.jensen.stock.vo.resp.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author jensen
 * @date 2024-08-27 7:56
 * @description 定义股票相关接口控制器
 */

@Tag(name = "定义股票相关接口控制器", description = "定义股票相关接口控制器")
@RestController
@RequestMapping("api/quot")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 获取国内大盘最新数据
     * @return
     */
    @Operation(summary = "获取国内大盘最新数据", description = "获取国内大盘最新数据")
    @GetMapping("index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
        return stockService.getInnerMarketInfo();
    }

    /**
     * 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    @Operation(summary = "获取沪深两市板块最新数据", description = "获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据")
    @GetMapping("sector/all")
    public R<List<StockBlockDomain>> sectorAll(){
        return stockService.sectorAllLimit();
    }

    /**
     * 获取国外大盘最新数据
     * @return
     */
    @Operation(summary = "获取国外大盘最新数据", description = "获取国外大盘最新数据")
    @GetMapping("external/index")
    public R<List<OuterMarketDomain>> getOuterMarketInfo(){
        return stockService.getOuterMarketInfo();
    }

    /**
     * 股票涨幅统计
     * @return
     */
    @Parameters({
            @Parameter(name = "page", description = "", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "", in = ParameterIn.QUERY)
    })
    @Operation(summary = "股票涨幅统计", description = "股票涨幅统计")
    @GetMapping("stock/all")
    public R<PageResult> getStockPageInfo(@RequestParam(name = "page",required = false,defaultValue = "1",value ="page") Integer page, @RequestParam(name = "pageSize",required = false,defaultValue = "20",value = "pageSize") Integer pageSize){
        return stockService.getStockPageInfo(page,pageSize);
    }

    /**
     * 统计沪深两市个股最新交易数据
     * @return
     */
    @Operation(summary = "统计沪深两市个股最新交易数据", description = "统计沪深两市个股最新交易数据")
    @GetMapping("stock/increase")
    public R<List<StockUpdownDomain>> getStockInfo(){
        return stockService.getStockInfo();
    }

    /**
     * 统计最新交易日下股票每分钟涨跌停的数量
     * @return
     */
    @Operation(summary = "统计最新交易日下股票每分钟涨跌停的数量", description = "统计最新交易日下股票每分钟涨跌停的数量")
    @GetMapping("/stock/updown/count")
    public R<Map> getStockUpdownCount(){
        return stockService.getStockUpdownCount();
    }

    /**
     * 将指定页的股票数据导出到excel表下
     * @param response
     * @param page  当前页
     * @param pageSize 每页大小
     */
    @Parameters({
            @Parameter(name = "page", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页大小", in = ParameterIn.QUERY)
    })
    @Operation(summary = "将指定页的股票数据导出到excel表下", description = "将指定页的股票数据导出到excel表下")
    @GetMapping("/stock/export")
    public void stockExport(HttpServletResponse response,
                            @RequestParam(name = "page",required = false,defaultValue = "1",value ="page") Integer page,
                            @RequestParam(name = "pageSize",required = false,defaultValue = "20",value = "pageSize") Integer pageSize){
        stockService.stockExport(response,page,pageSize);
    }

    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */
    @Operation(summary = "功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）", description = "功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）")
    @GetMapping("/stock/tradeAmt")
    public R<Map> stockTradeVol4InnerMarket(){
        return stockService.stockTradeVol4InnerMarket();
    }

    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     * @return
     */
    @Operation(summary = "查询当前时间下股票的涨跌幅度区间统计功能 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点", description = "查询当前时间下股票的涨跌幅度区间统计功能 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点")
    @GetMapping("/stock/updown")
    public R<Map> getStockUpDown(){
        return stockService.stockUpDownScopeCount();
    }

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    @Operation(summary = "功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点", description = "功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点")
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code){
        return stockService.stockScreenTimeSharing(code);
    }

    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    @Parameter(name = "code", description = "股票编码", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据", description = "单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据")
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getDayKLinData(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.stockCreenDkLine(stockCode);
    }

    /**
     * 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @param searchStr
     * @return
     */
    @Parameter(name = "searchStr", description = "", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "根据输入的个股代码，进行模糊查询，返回证券代码和证券名称", description = "根据输入的个股代码，进行模糊查询，返回证券代码和证券名称")
    @GetMapping("/stock/search")
    public R<List<Map>> getStocksByCode(@RequestParam(value = "searchStr",required = true) String searchStr){
        return stockService.getStocksByCode(searchStr);
    }

    /**
     * 个股描述
     * @param code
     * @return
     */
    @GetMapping("/stock/describe")
    public R<Map> getStockDescribe(@RequestParam(value = "code",required = true) String code){
        return stockService.getStockDescribe(code);
    }

    /**
     * 个股周K图展示
     * @param code
     * @return
     */
    @GetMapping("/stock/screen/weekkline")
    public R<List<Stock4WeekDomain>> getWeekKLine(@RequestParam(value = "code",required = true) String code){
        return stockService.stockCreenWkLine(code);
    }

    /**
     * 获取个股最新分时行情数据，主要包含：
     * 	开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;
     * @param code
     * @return
     */
    @GetMapping("/stock/screen/second/detail")
    public R<Map> getStockSecondDetail(@RequestParam(value = "code",required = true) String code){
        return stockService.getStockSecondDetail(code);
    }

    /**
     * 个股交易流水行情数据查询--查询最新交易流水，按照交易时间降序取前10
     * @param code
     * @return
     */
    @GetMapping("/stock/screen/second")
    public  R<List<Map>> getStockScreenSecond(@RequestParam(value = "code",required = true) String code){
        return stockService.getStockScreenSecond(code);
    }
}
