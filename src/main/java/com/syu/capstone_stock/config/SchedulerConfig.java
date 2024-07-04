package com.syu.capstone_stock.config;

import com.syu.capstone_stock.util.PythonExec;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {
    @Scheduled(fixedDelay = 3600000, initialDelay = 0)
    public void run(){
        try{
            PythonExec.execSchedule();
        } catch (Exception e){
            System.out.println("스케쥴러 오류");
        }
    }
}