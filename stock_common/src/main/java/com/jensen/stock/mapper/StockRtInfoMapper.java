package com.jensen.stock.mapper;

import com.jensen.stock.pojo.domain.Stock4EvrDayDomain;
import com.jensen.stock.pojo.domain.Stock4MinuteDomain;
import com.jensen.stock.pojo.domain.Stock4WeekDomain;
import com.jensen.stock.pojo.domain.StockUpdownDomain;
import com.jensen.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 59484
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:32
* @Entity com.jensen.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    List<StockUpdownDomain> getNewestStockInfo(Date curDate);

    List<StockUpdownDomain> getStockInfo(Date curDate);

    @MapKey("{count,time}")
    List<Map> getStockUpdownCount(@Param("openTime") Date openTime, @Param("curTime") Date curTime, @Param("flag") int flag);

    @MapKey("")
    List<Map> getStockUpDownSectionByTime(@Param("avlDate") Date avlDate);

    /**
     * 根据时间范围查询指定股票的交易流水
     * @param stockCode 股票code
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @return
     */
    List<Stock4MinuteDomain> getStockInfoByCodeAndDate(@Param("stockCode") String stockCode,
                                                       @Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    /**
     * 查询指定日期范围内指定股票每天的交易数据
     * @param stockCode 股票code
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @return
     */
    List<Stock4EvrDayDomain> getStockInfo4EvrDay(@Param("stockCode") String stockCode,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);

    /**
     * 批量插入功能
     * @param stockRtInfoList
     */
    int insertBatch(List<StockRtInfo> stockRtInfoList);

    /**
     * 个股周K图
     * @param code
     * @param starTime
     * @param endTime
     * @return
     */
    List<Stock4WeekDomain> getStockCreenWkLine(@Param("code") String code,
                                               @Param("starTime") Date starTime,
                                               @Param("endTime") Date endTime);

    /**
     * 根据编码和时间查询当前价
     * @param code
     * @param times
     * @return
     */
    List<BigDecimal> getStockInfoByCodeAndTimes(@Param("code") String code,
                                                @Param("times") List<Date> times);

    /**
     * 获取个股票最分时新行情数据
     * @param code
     * @param endDate
     * @return
     */
    Map<String, String> getStockNewPriceByCode(@Param("code") String code,
                                               @Param("endDate") Date endDate);
}
