package com.maidscc.maidscc_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MaidsccTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaidsccTaskApplication.class, args);
	}

}
