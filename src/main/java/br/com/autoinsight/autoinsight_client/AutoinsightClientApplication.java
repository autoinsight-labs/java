package br.com.autoinsight.autoinsight_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AutoinsightClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoinsightClientApplication.class, args);
	}

}
