package com.audioweb.serverPool.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.audioweb.common.config.datasource.DynamicDataSourceContextHolder;
import com.audioweb.serverPool.service.IClientService;

@Service
public class ClientServiceImpl implements IClientService {
	public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
	
	@Override
	//@Scheduled(cron = " */1 * * * * ? ")
	@Async("ServerServiceExecutor")
	public void executeAsync() {
		log.info("start executeAsync");
        System.out.println("异步线程执行批量插入等耗时任务");
        log.info("end executeAsync");		
	}
	
	@Override
	@Async("ServerServiceExecutor")
	public void execute(Runnable task) {
		// TODO Auto-generated method stub
		task.run();
	}
}