package com.prologapp.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.prologapp.desafio")
public class DesafioApiPrologAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioApiPrologAppApplication.class, args);
	}
}
