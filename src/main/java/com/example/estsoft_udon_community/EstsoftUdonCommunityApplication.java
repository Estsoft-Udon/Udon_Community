package com.example.estsoft_udon_community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EstsoftUdonCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstsoftUdonCommunityApplication.class, args);
    }

}
