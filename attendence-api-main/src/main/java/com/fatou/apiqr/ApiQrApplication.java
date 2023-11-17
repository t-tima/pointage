package com.fatou.apiqr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class ApiQrApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiQrApplication.class, args);
    }





}
