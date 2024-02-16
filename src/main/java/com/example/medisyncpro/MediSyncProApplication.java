package com.example.medisyncpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class MediSyncProApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediSyncProApplication.class, args);
    }

   /* @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }*/

}
