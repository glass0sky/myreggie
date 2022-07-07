package com.mdy.myreggie;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class MyReggieTakeOutApp {
    public static void main(String[] args) {
        log.info("项目开始启动...");
        ApplicationContext context = SpringApplication.run(MyReggieTakeOutApp.class, args);
        log.info("项目启动成功.....");
    }
}
