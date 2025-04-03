package com.gdmn.card_manager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class CardManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardManagerApplication.class, args);
	}

}
