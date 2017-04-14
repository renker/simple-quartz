package com.quartz.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.plugins.history.LoggingJobHistoryPlugin;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.quartz.proxy.QuartzProxyJob;

@Component
public class QuartzTool {
	
	@Resource
	private SchedulerFactoryBean schedulerFactoryBean;
	
	public Scheduler getScheduler(){
		return schedulerFactoryBean.getScheduler();
	}
	
	/**
	 * 创建任务
	 * @param jobName
	 * @param jobGroupName
	 * @param targetClass
	 * @param targetMethod
	 * @return
	 * @throws SchedulerException 
	 */
	public JobDetail jobCreate(String jobName,String jobGroupName,String targetClass,String targetMethod) throws SchedulerException{
		Map<String, Object> map = new HashMap<String,Object>();
		JobDataMap jobDataMap = new JobDataMap(map);
		jobDataMap.put("targetClass", targetClass);
		jobDataMap.put("targetMethod", targetMethod);
		JobDetail job = JobBuilder.newJob(QuartzProxyJob.class).setJobData(jobDataMap).withIdentity(jobName, jobGroupName).build();
		// getScheduler().addJob(job, replace);
		return job;
	}
	
	@SuppressWarnings("unchecked")
	public JobDetail jobCreate(String jobName,String jobGroupName,Class jobClass) throws SchedulerException{
		Map<String, Object> map = new HashMap<String,Object>();
		
//		JobDataMap jobDataMap = new JobDataMap(map);
//		jobDataMap.put("targetClass", targetClass);
//		jobDataMap.put("targetMethod", targetMethod);
		
		JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
		
		return job;
	}
	
	/**
	 * 创建触发器
	 * @param triggerName
	 * @param triggerGroupName
	 * @param cronExpression
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Trigger triggerCreate(String triggerName,String triggerGroupName,String cronExpression){
		ScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(schedBuilder).withIdentity(triggerName, triggerGroupName).build();
		return trigger;
	}
	
	/**
	 * 开始任务
	 * @param jobDetail
	 * @param trigger
	 * @throws SchedulerException
	 */
	public void startJob(JobDetail jobDetail,Trigger trigger) throws SchedulerException{
		Scheduler scheduler = getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		
		//scheduler.getListenerManager().addJobListener(new LoggingJobHistoryPlugin());
		scheduler.start();
	}
	
	/**
	 * 暂停单个任务
	 * @param jobKey
	 * @throws SchedulerException
	 */
	public void pauseJob(JobKey jobKey) throws SchedulerException{
		getScheduler().pauseJob(jobKey);
	}
	
	public void resumeJob(JobKey jobKey) throws SchedulerException{
		getScheduler().resumeJob(jobKey);
	}
	
	/**
	 * 暂停触发器与任务
	 * @param triggerKey
	 * @throws SchedulerException 
	 */
	public void removeJobAndTrigger(JobKey jobKey ,TriggerKey triggerKey) throws SchedulerException{
		TriggerState triggerState  = getScheduler().getTriggerState(triggerKey);
		
		if(triggerState != TriggerState.PAUSED){
			getScheduler().pauseTrigger(triggerKey);
		}
		
		getScheduler().unscheduleJob(triggerKey);
		getScheduler().deleteJob(jobKey);
	}
	
	@SuppressWarnings("unchecked")
	public void list(){
		try {
			List<String> jobGroupNames = getScheduler().getJobGroupNames();
			for (String jobGroupName : jobGroupNames) {
				 Set<JobKey> jobKeys = getScheduler().getJobKeys(GroupMatcher.groupEquals(jobGroupName));
				 for (JobKey jobKey : jobKeys) {
					 JobDetail jobDetail = getScheduler().getJobDetail(jobKey);
					 System.out.println("--------------------------------------------------------");
					 System.out.println("任务类名：\t\t"+jobDetail.getJobClass().getName());
					 System.out.println("任务名：\t\t"+jobKey.getName());
					 System.out.println("任务组名：\t\t"+jobKey.getGroup());
					 List<Trigger> triggers = (List<Trigger>) getScheduler().getTriggersOfJob(jobKey);
					 for (Trigger trigger : triggers) {
						 System.out.println("触发器名： \t\t"+trigger.getKey().getName());
						 System.out.println("触发器组名： \t\t"+trigger.getKey().getGroup());
						 
						 TriggerState triggerState = getScheduler().getTriggerState(trigger.getKey());
						 System.out.println("任务状态： \t\t"+status(triggerState));
						 
//						 System.out.println("上次执行时间：\t"+formatDate(trigger.getPreviousFireTime()));
//						 System.out.println("下次预计执行时间：\t"+formatDate(trigger.getNextFireTime()));
					 }
					 System.out.println("--------------------------------------------------------");
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public String status(TriggerState status){
		if(status == TriggerState.NORMAL){
			return "正常";
		}else if(status == TriggerState.PAUSED){
			return "暂停";
		}else{
			return "";
		}
	}
	
	public String formatDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
