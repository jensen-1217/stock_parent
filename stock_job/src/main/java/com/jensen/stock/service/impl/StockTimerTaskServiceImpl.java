package com.jensen.stock.service.impl;

import com.google.common.collect.Lists;
import com.jensen.stock.constant.ParseType;
import com.jensen.stock.mapper.StockBusinessMapper;
import com.jensen.stock.mapper.StockMarketIndexInfoMapper;
import com.jensen.stock.mapper.StockRtInfoMapper;
import com.jensen.stock.pojo.entity.StockMarketIndexInfo;
import com.jensen.stock.pojo.entity.StockRtInfo;
import com.jensen.stock.pojo.vo.StockInfoConfig;
import com.jensen.stock.service.StockTimerTaskService;
import com.jensen.stock.utils.DateTimeUtil;
import com.jensen.stock.utils.IdWorker;
import com.jensen.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author jensen
 * @date 2024-09-05 21:32
 * @description
 */
@Service("stockTimerTaskService")
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    //注入格式解析bean
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private HttpEntity<String> entity;

    @Override
    public void getInnerMarketInfo() {
        //1.定义采集的url接口
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
        //2.调用restTemplate采集数据
        //2.1 组装请求头
//        HttpHeaders headers = new HttpHeaders();
//        //必须填写，否则数据采集不到
//        headers.add("Referer", "https://finance.sina.com.cn/stock/");
//        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
//        //2.2 组装请求对象
//        HttpEntity<Object> entity = new HttpEntity<>(headers);
        //2.3 resetTemplate发起请求
//        String resString = restTemplate.postForObject(url, entity, String.class);
        //log.info("当前采集的数据：{}",resString);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            log.error("点前时间点：{},采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
            return;
        }
        String jsData = responseEntity.getBody();
        log.info("点前时间点：{},采集的原始数据:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), jsData);
        //3.数据解析（重要）
//        var hq_str_sh000001="上证指数,3267.8103,3283.4261,3236.6951,3290.2561,3236.4791,0,0,402626660,398081845473,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2022-04-07,15:01:09,00,";
//        var hq_str_sz399001="深证成指,12101.371,12172.911,11972.023,12205.097,11971.334,0.000,0.000,47857870369,524892592190.995,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,2022-04-07,15:00:03,00";
        //正则表达式
        String reg = "var hq_str_(.+)=\"(.+)\";";
        //编译正则表达式
        Pattern pattern = Pattern.compile(reg);
        //匹配字符串
        Matcher matcher = pattern.matcher(jsData);
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        //判断是否有匹配的数值
        while (matcher.find()) {
            String marketCode = matcher.group(1);
            String other = matcher.group(2);
            String[] splitArr = other.split(",");
            //大盘名称
            String marketName = splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint = new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint = new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint = new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint = new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint = new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt = Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol = new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //组装entity对象
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();

            //收集封装的对象，方便批量插入
            list.add(info);
        }
        log.info("采集的当前大盘数据：{}", list);
        //批量插入
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //TODO 后续完成批量插入功能
        int count= stockMarketIndexInfoMapper.insertBatch(list);
        if (count>0) {
            //通知后台终端刷新本地缓存，发送的日期数据是告知对方当前更新的股票数据所在时间点
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());
            log.info("当前时间：{},批量插入大盘数据：{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }else {
            log.error("当前时间：{},批量插入大盘数据：{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }
    }

    /**
     * 批量获取股票分时数据详情信息
     * http://hq.sinajs.cn/list=sz000002,sh600015
     */
    @Override
    public void getStockRtIndex() {
        //批量获取股票ID集合
        List<String> stockIds = stockBusinessMapper.getStockIds();
        //计算出符合sina命名规范的股票id数据
        stockIds = stockIds.stream().map(id -> {
            return id.startsWith("6") ? "sh" + id : "sz" + id;
        }).collect(Collectors.toList());
        //设置公共请求头对象
        //设置请求头数据
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer","https://finance.sina.com.cn/stock/");
//        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
        //一次性查询过多，我们将需要查询的数据先进行分片处理，每次最多查询20条股票数据
        long startTime = System.currentTimeMillis();
        Lists.partition(stockIds,20).forEach(list->{
//            //拼接股票url地址
//            String stockUrl=stockInfoConfig.getMarketUrl()+String.join(",",list);
//            //获取响应数据
//            String result = restTemplate.postForObject(stockUrl,entity,String.class);
//            ResponseEntity<String> responseEntity = restTemplate.exchange(stockUrl, HttpMethod.GET, entity, String.class);
//            int statusCodeValue = responseEntity.getStatusCodeValue();
//            if (statusCodeValue != 200) {
//                log.error("点前时间点：{},采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
//                return;
//            }
//            List<StockRtInfo> infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
//            log.info("数据：{}",infos);
//            int count = stockRtInfoMapper.insertBatch(infos);
//            if (count>0) {
//                log.info("当前时间：{},批量插入个股数据：{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
//            }else {
//                log.error("当前时间：{},批量插入个股数据：{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
//            }
//            //方案2：
//            new Thread(()->{
//                //拼接股票url地址
//                String stockUrl=stockInfoConfig.getMarketUrl()+String.join(",",list);
//                //获取响应数据
//                String result = restTemplate.postForObject(stockUrl,entity,String.class);
//                ResponseEntity<String> responseEntity = restTemplate.exchange(stockUrl, HttpMethod.GET, entity, String.class);
//                int statusCodeValue = responseEntity.getStatusCodeValue();
//                if (statusCodeValue != 200) {
//                    log.error("点前时间点：{},采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
//                    return;
//                }
//                List<StockRtInfo> infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
//                log.info("数据：{}",infos);
//                //TODO 批量插入
//                int count = stockRtInfoMapper.insertBatch(infos);
//                if (count>0) {
//                    log.info("当前时间：{},批量插入个股数据：{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
//                }else {
//                    log.error("当前时间：{},批量插入个股数据：{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
//                }
//            }).start();
            //方案3：
            threadPoolTaskExecutor.execute(()->{
                //拼接股票url地址
                String stockUrl=stockInfoConfig.getMarketUrl()+String.join(",",list);
                //获取响应数据
                String result = restTemplate.postForObject(stockUrl,entity,String.class);
                ResponseEntity<String> responseEntity = restTemplate.exchange(stockUrl, HttpMethod.GET, entity, String.class);
                int statusCodeValue = responseEntity.getStatusCodeValue();
                if (statusCodeValue != 200) {
                    log.error("点前时间点：{},采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
                    return;
                }
                List<StockRtInfo> infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
                log.info("数据：{}",infos);
                //TODO 批量插入
                int count = stockRtInfoMapper.insertBatch(infos);
                if (count>0) {
                    log.info("当前时间：{},批量插入个股数据：{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
                }else {
                    log.error("当前时间：{},批量插入个股数据：{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),infos);
                }
            });
        });
        long takeTime = System.currentTimeMillis()-startTime;
        log.info("本次采集花费时间：{}",takeTime);
    }

    @PostConstruct
    public void initData(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }
}
