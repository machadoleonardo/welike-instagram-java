package br.com.welike.instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InstagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramApplication.class, args);
	}

}

//	ALTER TABLE
//    influencer
//			CHANGE full_name full_name
//			VARCHAR(255)
//			CHARACTER SET utf8mb4
//			COLLATE utf8mb4_unicode_ci;
//"//*[@role='dialog']/descendant::a[@title]/text()"