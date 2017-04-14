package com.quartz.plugin;

import java.text.MessageFormat;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingJobDatabasePlugin implements SchedulerPlugin ,JobListener{
	Logger logger = LoggerFactory.getLogger(SchedulerPlugin.class);
	
	private String jobToBeFiredMessage = "Job {1}.{0} fired (by trigger {4}.{3}) at: {2, date, HH:mm:ss MM/dd/yyyy}";

    private String jobSuccessMessage = "Job {1}.{0} execution complete at {2, date, HH:mm:ss MM/dd/yyyy} and reports: {8}";

    private String jobFailedMessage = "Job {1}.{0} execution failed at {2, date, HH:mm:ss MM/dd/yyyy} and reports: {8}";

    private String jobWasVetoedMessage = "Job {1}.{0} was vetoed.  It was to be fired (by trigger {4}.{3}) at: {2, date, HH:mm:ss MM/dd/yyyy}";
    
    private String name;

	@Override
	public void initialize(String name, Scheduler scheduler, ClassLoadHelper loadHelper) throws SchedulerException {
		logger.debug("*********************************************************");
		logger.debug("初始化中......");
		logger.debug("*********************************************************");
		
		this.name = name;
	    scheduler.getListenerManager().addJobListener(this, EverythingMatcher.allJobs());
	}

	@Override
	public void start() {
		logger.debug("*********************************************************");
		logger.debug("启动......");
		logger.debug("*********************************************************");
	}

	@Override
	public void shutdown() {
		logger.debug("*********************************************************");
		logger.debug("停止......");
		logger.debug("*********************************************************");
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * 记录即将执行的任务
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		Object [] args = {
			context.getJobDetail().getKey().getName(),
			context.getJobDetail().getKey().getGroup(),
			context.getTrigger().getPreviousFireTime(),
			context.getTrigger().getKey().getName(),
			context.getTrigger().getKey().getGroup()
		};
		logger.debug(MessageFormat.format(jobToBeFiredMessage, args));
	}
	
	/**
	 * 记录任务执行结果
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		
	}

	/**
	 * 记录否决的任务
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		
	}

	

}
