package com.CasaFutura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//public class CasaPiuApplication {
public class CasaFuturaApplication extends SpringBootServletInitializer  {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CasaFuturaApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CasaFuturaApplication.class, args);
		
	}
}
