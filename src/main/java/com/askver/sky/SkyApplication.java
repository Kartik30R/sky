package com.askver.sky;

import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {PgVectorStoreAutoConfiguration.class})
public class SkyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkyApplication.class, args);
	}

}
