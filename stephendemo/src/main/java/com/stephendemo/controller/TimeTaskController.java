package com.stephendemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author jmfen
 * 使用goCron测试任务调度
 * date 2020-04-18
 */


@RestController
@Slf4j
public class TimeTaskController {

    @GetMapping("/time/task1")
    public String task1(){
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        log.info("task1 do at {}", time);
        return time;
    }

    @GetMapping("/time/task1_1")
    public String task1_1(){
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        log.info("task1_1 success, do at {}", time);
        return "task1_1 success";
    }
}