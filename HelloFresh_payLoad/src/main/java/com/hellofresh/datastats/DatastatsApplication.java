package com.hellofresh.datastats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.hellofresh.datastats","EventController","StatsController","service"})
public class DatastatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatastatsApplication.class, args);
    }

}