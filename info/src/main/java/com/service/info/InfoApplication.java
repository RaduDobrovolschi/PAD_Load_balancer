package com.service.info;

import com.service.info.config.CRLFLogConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class InfoApplication {

	private static final Logger log = LoggerFactory.getLogger(InfoApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(InfoApplication.class, args);
		log.info(CRLFLogConverter.CRLF_SAFE_MARKER, "\n--------------------------------------------\n\t\tApplication is running!\n--------------------------------------------");
	}
}
