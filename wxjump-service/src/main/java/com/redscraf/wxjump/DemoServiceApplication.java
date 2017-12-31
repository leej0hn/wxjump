package com.redscraf.wxjump;

import com.redscraf.wxjump.service.component.BackgroundImage4Panel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;


/**
 * <p>function:
 * <p>User: leejohn
 * <p>Date: 16/7/8
 * <p>Version: 1.0
 */
@SpringBootApplication
@Slf4j
public class DemoServiceApplication  implements CommandLineRunner {

    @Value("${spring.application.name}")
    private String applicationName;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless","false");
        SpringApplication application = new SpringApplication(DemoServiceApplication.class);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        log.info("{} boot runing", this.applicationName);
        BackgroundImage4Panel.start();
        countDownLatch.await();
    }
}