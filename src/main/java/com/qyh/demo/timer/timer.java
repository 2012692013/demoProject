package com.qyh.demo.timer;

import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * author:qiuyuehao
 * Date:2018/10/15
 * Time:下午5:32
 */
@Component
public class timer {/*

    //每5秒执行
    @Scheduled(cron = "0/5 * * * * ?")
    void deleteUnUseIndent(){
        System.out.println(123);
    }

    *//**
     * 上一次 启动时间点之后 X秒执行一次
     *//*
    @Scheduled(fixedRate = 5000)
    public void timerToZZP(){
        System.out.println("ZZP:" + new Random().nextLong() + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    *//**
     * 上一次 结束时间点之后 每X秒执行一次
     *//*
    @Scheduled(fixedDelay = 50000)
    public void timerToReportCount(){
        for (int i = 0; i < 10; i++){
            System.out.println("<================its" + i + "count===============>" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }
    }

    *//**
     * 第一次延迟 X秒执行，之后按照fixedRate的规则每X秒执行
     *//*
    @Scheduled(initialDelay = 50000,fixedRate = 6000)
    public void timerToReport(){
        for (int i = 0; i < 10; i++){
            System.out.println("<================delay :" + i + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "count===============>");
        }
    }*/

}
