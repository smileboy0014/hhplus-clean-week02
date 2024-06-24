package com.hhplus.clean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HhplusCleanJavaWeek02Application {

    public static void main(String[] args) {
        SpringApplication.run(HhplusCleanJavaWeek02Application.class, args);
    }

}
