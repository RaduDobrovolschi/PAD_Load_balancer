package com.service.smart.proxy;

import com.service.smart.proxy.config.CRLFLogConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//https://spring.io/guides/gs/spring-cloud-loadbalancer/
@SpringBootApplication
public class ProxyApplication {
	private static final Logger log = LoggerFactory.getLogger(ProxyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
		log.info(CRLFLogConverter.CRLF_SAFE_MARKER, "\n--------------------------------------------\n\t\tApplication is running!\n--------------------------------------------");
	}

}
