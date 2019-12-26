package com.wsh.quartzdemo.controller;

import com.wsh.quartzdemo.entity.TaskEntity;
import com.wsh.quartzdemo.job.TaskJob;
import com.wsh.quartzdemo.service.QuartzJobService;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jmfen
 * date 2019-12-24
 */

@RestController
public class TaskController {

    @Autowired
    private QuartzJobService quartzJobService;

    //这个定时任务的名称和分组
    private final JobKey jobKey = JobKey.jobKey("example", "GroupOne");

    @GetMapping("/task/example")
    public String startExampleTask() throws SchedulerException {
        //创建一个定时任务
        TaskEntity task = TaskEntity.builder()
                .jobKey(jobKey)
                .cronExpression("0/2 * * * * ? ")   //定时任务 的cron表达式
                .jobClass(TaskJob.class)   //定时任务 的具体执行逻辑
                .description("这是一个测试的 任务")    //定时任务 的描述
                .build();

        quartzJobService.scheduleJob(task);
        return "succeed.";
    }

    /**
     * 暂停 hello world
     */
    @GetMapping("/pauseExampleJob")
    public String pauseHelloWorldJob() throws SchedulerException {
        quartzJobService.pauseJob(jobKey);
        return "pauseExampleJob success";
    }

    /**
     * 恢复 hello world
     */
    @GetMapping("/resumeExampleJob")
    public String resumeExampleJob() throws SchedulerException {
        quartzJobService.resumeJob(jobKey);
        return "resumeExampleJob success";
    }

    /**
     * 删除 hello world
     */
    @GetMapping("/deleteExampleJob")
    public String deleteExampleJob() throws SchedulerException {
        quartzJobService.deleteJob(jobKey);
        return "deleteExampleJob success";
    }

    /**
     * 修改 hello world 的cron表达式
     */
    @GetMapping("/modifyHelloWorldJobCron")
    public String modifyHelloWorldJobCron() {

        //这是即将要修改cron的定时任务
        TaskEntity modifyCronTask = TaskEntity.builder()
                .jobKey(jobKey)
                .cronExpression("0/5 * * * * ? ")   //定时任务 的cron表达式
                .jobClass(TaskJob.class)   //定时任务 的具体执行逻辑
                .description("这是一个测试的 任务")    //定时任务 的描述
                .build();
        if (quartzJobService.modifyJobCron(modifyCronTask))
            return "modifyHelloWorldJobCron success";
        else
            return "modifyHelloWorldJobCron fail";
    }
}