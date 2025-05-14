package com.Rede_Social;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableAsync
public class RedeSocialApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RedeSocialApplication.class, args);
	}

}
