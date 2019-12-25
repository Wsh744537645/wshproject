package com.wsh.quartzdemo.entity;

import lombok.Builder;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobKey;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author jmfen
 * date 2019-12-24
 */

@Data
@Builder
public class TaskEntity {
    /**
     * 定时任务 的名字和分组名 JobKey,{@link org.quartz.JobKey}
     */
    @NotNull(message = "定时任务的 名字 和 组名 坚决不为空")
    private JobKey jobKey;

    /**
     * 定时任务 的描述(可以定时任务本身的描述,也可以是触发器的)
     * {@link org.quartz.JobDetail} description ; {@link org.quartz.Trigger} description
     */
    private String description;

    /**
     * 定时任务 的执行cron (Trigger的CronScheduleBuilder 的cronExpression)
     * {@link org.quartz.Trigger} CronScheduleBuilder {@link org.quartz.CronScheduleBuilder}
     */
    @NotEmpty(message = "定时任务的执行cron 不能为空")
    private String cronExpression;

    /**
     * 定时任务 的元数据
     * {@link org.quartz.JobDataMap}
     */
    private Map<?, ?> jobDataMap;

    /**
     * 定时任务 的 具体执行逻辑类
     * {@link org.quartz.Job}
     */
    @NotNull(message = "定时任务的具体执行逻辑类 坚决不能为空")
    private Class<? extends Job> jobClass;
}