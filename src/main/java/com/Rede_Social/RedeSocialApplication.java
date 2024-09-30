package com.Rede_Social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedeSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedeSocialApplication.class, args);
	}

}
