package com.service.smart.proxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Boolean enableCaching;

    public boolean cachingEnabled() {
        return Boolean.TRUE.equals(enableCaching);
    }

    public void setEnableCaching(Boolean enableCaching) {
        this.enableCaching = enableCaching;
    }
}
