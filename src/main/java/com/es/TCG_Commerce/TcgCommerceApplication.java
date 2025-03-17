package com.es.TCG_Commerce;

import com.es.TCG_Commerce.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableAsync
public class TcgCommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcgCommerceApplication.class, args);
	}

}
