package com.es.tcg_exchange;

import com.es.tcg_exchange.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableAsync
public class TcgExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcgExchangeApplication.class, args);
	}

}
