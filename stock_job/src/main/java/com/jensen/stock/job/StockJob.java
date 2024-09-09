package com.jensen.stock.job;

import com.jensen.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义股票相关数据的定时任务
 * @author laofang
 */
@Component
public class StockJob {
    /**
     * 注入股票定时任务服务bean
     */
    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    /**
     * 定时采集A股数据
     */
    @XxlJob("getStockInfos")
    public void getStockInfos(){
        stockTimerTaskService.getStockRtIndex();
    }


    /**
     * 定义定时任务，采集国内大盘数据
     */
    @XxlJob("getStockInnerMarketInfos")
    public void getStockInnerMarketInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    @XxlJob("jensen_job_test")
    public void jobTest(){
        System.out.println("jobTest run.....");
    }
}