package com.solidabis.koodihaaste22;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Koodihaaste22Application {

	public static void main(String[] args) {
		SpringApplication.run(Koodihaaste22Application.class, args);
	}

}
