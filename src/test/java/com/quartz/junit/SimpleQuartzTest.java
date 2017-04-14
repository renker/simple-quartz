package com.quartz.junit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.quartz.jobs.SimpleJob;
import com.quartz.tools.QuartzTool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml"})
public class SimpleQuartzTest {
	
	@Resource
	private QuartzTool quartzTool;
	
	private String jobName = "mySimpleJob";
	private String jobGroupName = "simple_group";
	private String triggerName = "simple_trigger";
	private String triggerGroupName = "simple_trigger_group";
	
	private String cronExpression = "*/20 * * * * ?";
	
	@Test
	public void simpleJobCreae(){
		try {
			
			JobDetail jobDetail = quartzTool.jobCreate(jobName, jobGroupName, SimpleJob.class);
			// 创建触发器
			Trigger trigger = quartzTool.triggerCreate(triggerName, triggerGroupName, cronExpression);
			quartzTool.startJob(jobDetail, trigger);
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void pauseJob() throws SchedulerException{
		quartzTool.pauseJob(new JobKey(jobName,jobGroupName));
	}
	
	
	@Test
	public void resumeJob() throws SchedulerException{
		quartzTool.resumeJob(new JobKey(jobName,jobGroupName));
	}
	
	
	@Test
	public void clean(){
		try {
			JobKey jobKey = new JobKey(jobName, jobGroupName);
			TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
			quartzTool.removeJobAndTrigger(jobKey, triggerKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void run(){
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void list(){
		quartzTool.list();
	}
}
