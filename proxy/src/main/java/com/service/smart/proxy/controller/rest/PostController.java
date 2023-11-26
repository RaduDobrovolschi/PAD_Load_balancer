package com.service.smart.proxy.controller.rest;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.service.smart.proxy.config.AppProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rest")
public class PostController {
    private final RestTemplate lbRestTemplate;
    private final RestTemplate defaultRestTemplate;
    private final String mainServiceUrl;

    private LoadingCache<String, ResponseEntity<Object>> primitiveCache = null;


    public PostController(@Qualifier("loadBalanced") RestTemplate lbRestTemplate,
                          @Qualifier("default") RestTemplate defaultRestTemplate,
                          SimpleDiscoveryProperties simpleDiscoveryProperties,
                          AppProperties appProperties) {
        this.lbRestTemplate = lbRestTemplate;
        this.defaultRestTemplate = defaultRestTemplate;

        DefaultServiceInstance mainService = simpleDiscoveryProperties.getInstances().get("service1").get(0);
        this.mainServiceUrl = String.format("http://%s:%s", mainService.getHost(), mainService.getPort());

        if (appProperties.cachingEnabled()) {
            primitiveCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(
                    new CacheLoader<>() {

                        @NonNull
                        @Override
                        public ResponseEntity<Object> load(@NonNull String key) throws Exception {
                            return exchangeGetRequests(key);
                        }
                });
        }
    }

    @GetMapping(value = "/**")
    public ResponseEntity<Object> handleGetRequests(HttpServletRequest request) throws ExecutionException {
        if (primitiveCache != null) {
            return primitiveCache.get(request.getRequestURI());
        } else {
            return exchangeGetRequests(request.getRequestURI());
        }
    }

    private ResponseEntity<Object> exchangeGetRequests(String path) {
        try {
            return lbRestTemplate.exchange(
                    "http://service1" + path,
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    Object.class
            );
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }

    }

    @PostMapping(value = "/**")
    public ResponseEntity<Object> handlePostRequests(HttpServletRequest request, @RequestBody String post) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", request.getContentType());
        cleanCache();
        try {
            return defaultRestTemplate.exchange(
                mainServiceUrl + request.getRequestURI(),
                HttpMethod.POST,
                new HttpEntity<>(post, headers),
                Object.class
            );
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }
    }

    @PutMapping(value = "/**")
    public ResponseEntity<Object> handlePutRequests(HttpServletRequest request, @RequestBody String post) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", request.getContentType());
        cleanCache();
        try {
            return defaultRestTemplate.exchange(
                    mainServiceUrl + request.getRequestURI(),
                    HttpMethod.PUT,
                    new HttpEntity<>(post, headers),
                    Object.class
            );
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }
    }

    @DeleteMapping("/**")
    public ResponseEntity<Void> handleDelete(HttpServletRequest request) {
        cleanCache();
        try {
            return defaultRestTemplate.exchange(
                mainServiceUrl + request.getRequestURI(),
                HttpMethod.DELETE,
                new HttpEntity<>(null, null),
                Void.class
            );
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode());
        }
    }

    private void cleanCache() {
        if (primitiveCache != null) {
            primitiveCache.invalidateAll();
        }
    }
}
