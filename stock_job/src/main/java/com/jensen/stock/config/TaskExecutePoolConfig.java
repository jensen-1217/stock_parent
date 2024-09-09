package com.jensen.stock.config;

import com.jensen.stock.pojo.vo.TaskThreadPoolInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jensen
 * @date 2024-09-08 20:06
 * @description
 */
@Configuration
public class TaskExecutePoolConfig {
    @Autowired
    private TaskThreadPoolInfo info;

    @Bean(name = "threadPoolTaskExecutor",destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(info.getCorePoolSize());
        executor.setMaxPoolSize(info.getMaxPoolSize());
        executor.setKeepAliveSeconds(info.getKeepAliveSeconds());
        executor.setQueueCapacity(info.getQueueCapacity());
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
