package com.jensen.stock.mapper;

import com.jensen.stock.pojo.domain.OuterMarketDomain;
import com.jensen.stock.pojo.entity.StockOuterMarketIndexInfo;

import java.util.Date;
import java.util.List;

/**
* @author 59484
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:32
* @Entity com.jensen.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    List<OuterMarketDomain> getMarket(Date curDate);
}
