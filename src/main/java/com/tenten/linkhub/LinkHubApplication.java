package com.tenten.linkhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class LinkHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkHubApplication.class, args);
    }

}
