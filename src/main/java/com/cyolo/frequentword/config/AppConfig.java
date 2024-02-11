package com.cyolo.frequentword.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
public class AppConfig {
    @Bean
    public Queue<String> wordQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}

