package com.hamdan.agiticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AgiticketApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgiticketApplication.class, args);
	}

}
