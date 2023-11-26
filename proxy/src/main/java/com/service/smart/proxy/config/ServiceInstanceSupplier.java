package com.service.smart.proxy.config;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

public class ServiceInstanceSupplier implements ServiceInstanceListSupplier {

    private final String serviceId;
    private final List<DefaultServiceInstance> services;

    public ServiceInstanceSupplier(String serviceId,List<DefaultServiceInstance> services) {
        this.serviceId = serviceId;
        this.services = services;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(
                services.stream()
                    .map(service ->
                        new DefaultServiceInstance(serviceId + service.getInstanceId(), serviceId, service.getHost(), service.getPort(), service.isSecure())
                    ).map(o -> (ServiceInstance)o)
                    .toList()
        );
    }
}
