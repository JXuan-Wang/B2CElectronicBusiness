package org.example.system.manager;

import org.example.system.common.log.annotation.EnableLogAspect;
import org.example.system.manager.properties.MinioProperties;
import org.example.system.manager.properties.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({UserProperties.class, MinioProperties.class})
@ComponentScan(basePackages = {"org.example.system"})
@EnableScheduling
@EnableLogAspect
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
