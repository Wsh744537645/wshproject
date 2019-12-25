package com.wsh.quartzdemo.service;

import com.wsh.quartzdemo.entity.TaskEntity;
import com.wsh.quartzdemo.job.TaskJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author jmfen
 * date 2019-12-24
 */

@Slf4j
@Service
public class QuartzJobService {
    //Quartz定时任务核心的功能实现类
    private Scheduler scheduler;

    public QuartzJobService(@Autowired SchedulerFactoryBean schedulerFactoryBean){
        scheduler = schedulerFactoryBean.getScheduler();
    }

    public void scheduleJob(TaskEntity taskEntity) throws SchedulerException{
        //1.定时任务 的 名字和组名
        JobKey jobKey = taskEntity.getJobKey();
        //2.定时任务 的 元数据
        JobDataMap jobDataMap = getJobDataMap(taskEntity.getJobDataMap());
        //3.定时任务 的 描述
        String description = taskEntity.getDescription();
        //4.定时任务 的 逻辑实现类
        Class<? extends Job> jobClass = taskEntity.getJobClass();
        //5.定时任务 的 cron表达式
        String cron = taskEntity.getCronExpression();
        JobDetail jobDetail = getJobDetail(jobKey, description, jobDataMap, jobClass);
        Trigger trigger = getTrigger(jobKey, description, jobDataMap, cron);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 获取定时任务的定义
     * JobDetail是任务的定义,Job是任务的执行逻辑
     *
     * @param jobKey      定时任务的名称 组名
     * @param description 定时任务的 描述
     * @param jobDataMap  定时任务的 元数据
     * @param jobClass    {@link org.quartz.Job} 定时任务的 真正执行逻辑定义类
     */
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap jobDataMap, Class<? extends Job> jobClass) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(jobDataMap)
                .usingJobData(jobDataMap)
                .requestRecovery()
                .storeDurably()
                .build();
    }

    /**
     * 获取Trigger (Job的触发器,执行规则)
     *
     * @param jobKey         定时任务的名称 组名
     * @param description    定时任务的 描述
     * @param jobDataMap     定时任务的 元数据
     * @param cronExpression 定时任务的 执行cron表达式
     */
    public Trigger getTrigger(JobKey jobKey, String description, JobDataMap jobDataMap, String cronExpression) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .withDescription(description)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .usingJobData(jobDataMap)
                .build();
    }

    public JobDataMap getJobDataMap(Map<?, ?> map) {
        return map == null ? new JobDataMap() : new JobDataMap(map);
    }
}