package com.service.smart.proxy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoadBalancerConfig {

    private static final Logger log = LoggerFactory.getLogger(LoadBalancerConfig.class);

    private final SimpleDiscoveryProperties simpleDiscoveryProperties;

    public LoadBalancerConfig(SimpleDiscoveryProperties simpleDiscoveryProperties) {
        this.simpleDiscoveryProperties = simpleDiscoveryProperties;
    }

    @Bean
    public ServiceInstanceListSupplier instanceSupplier(ConfigurableApplicationContext context) {
        List<DefaultServiceInstance> instances = simpleDiscoveryProperties.getInstances().get("service1");
        log.info("Initializing load balancing for service1: {}", instances);
        return ServiceInstanceListSupplier.builder()
                .withBase(new ServiceInstanceSupplier("service1", instances))
                .withCaching()
                .build(context);
    }
}
