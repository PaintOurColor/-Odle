package com.paintourcolor.odle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OdleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdleApplication.class, args);
    }

}
