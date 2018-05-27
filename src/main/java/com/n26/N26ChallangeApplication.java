package com.n26;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author taimur
 */

@SpringBootApplication
public class N26ChallangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(N26ChallangeApplication.class, args);
	}
	
	
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("TransactionService");
        executor.initialize();
        return executor;
    }
}
