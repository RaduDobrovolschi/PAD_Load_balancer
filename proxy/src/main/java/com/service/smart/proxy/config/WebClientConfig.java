package com.service.smart.proxy.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@LoadBalancerClient(name = "service1", configuration = LoadBalancerConfig.class)
public class WebClientConfig {

    @LoadBalanced
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Primary
    @LoadBalanced
    @Bean(name = "loadBalanced")
    RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "default")
    RestTemplate defaultRestTemplate() {
        return new RestTemplate();
    }
}