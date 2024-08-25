package com.jensen.stock.mapper;

import com.jensen.stock.pojo.entity.StockBusiness;

/**
* @author 59484
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-08-24 11:27:32
* @Entity com.jensen.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

}
