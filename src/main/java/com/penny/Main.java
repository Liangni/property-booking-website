package com.penny;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.penny.dao")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
