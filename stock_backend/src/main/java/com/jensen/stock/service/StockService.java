package com.jensen.stock.service;

import com.jensen.stock.pojo.domain.*;
import com.jensen.stock.vo.resp.PageResult;
import com.jensen.stock.vo.resp.R;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jensen
 * @date 2024-08-27 8:01
 * @description:
 */
public interface StockService {
    R<List<InnerMarketDomain>> getInnerMarketInfo();

    R<List<StockBlockDomain>> sectorAllLimit();

    R<List<OuterMarketDomain>> getOuterMarketInfo();

    R<PageResult> getStockPageInfo(Integer page, Integer pageSize);

    R<List<StockUpdownDomain>> getStockInfo();

    /**
     * 统计最新交易日下股票每分钟涨跌停的数量
     * @return
     */
    R<Map> getStockUpdownCount();

    /**
     * 将指定页的股票数据导出到excel表下
     * @param response
     * @param page  当前页
     * @param pageSize 每页大小
     */
    void stockExport(HttpServletResponse response, Integer page, Integer pageSize);

    R<Map> stockTradeVol4InnerMarket();
    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     * @return
     */
    R<Map> stockUpDownScopeCount();

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code);

    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param code 股票编码
     */
    R<List<Stock4EvrDayDomain>> stockCreenDkLine(String code);

    /**
     * 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @param searchStr
     * @return
     */
    R<List<Map>> getStocksByCode(String searchStr);

    R<Map> getStockDescribe(String code);

    R<List<Stock4WeekDomain>> stockCreenWkLine(String code);
}
