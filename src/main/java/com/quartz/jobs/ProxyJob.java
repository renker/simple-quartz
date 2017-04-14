package com.quartz.jobs;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 被代理任务
 */
@Component
public class ProxyJob {
	Logger logger = LoggerFactory.getLogger(ProxyJob.class);
	
	@SuppressWarnings("static-access")
	public void myTast(JobExecutionContext context) throws InterruptedException{
			logger.debug("[▲任务开始-------------------------------------]");
		
			for (int i = 5; i > 0; i--) {
				logger.info("▲任务执行中。。。。。倒计时=>"+i);
				Thread.currentThread().sleep(1000);
			}
		
			logger.debug("[▲任务结束-------------------------------------]");
		
	}
}
