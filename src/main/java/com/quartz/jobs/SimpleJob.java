package com.quartz.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class SimpleJob implements Job{
	Logger logger = LoggerFactory.getLogger(ProxyJob.class);
	
	@SuppressWarnings("static-access")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("[任务开始-------------------------------------]");
		
		for (int i = 25; i > 0; i--) {
			logger.info("任务执行中。。。。。倒计时=>"+i);
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		logger.debug("[任务结束-------------------------------------]");
	}
}
