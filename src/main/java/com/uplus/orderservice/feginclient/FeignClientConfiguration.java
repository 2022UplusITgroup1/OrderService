package com.uplus.orderservice.feginclient;

import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer.Target;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // feign.Target<?> target = requestTemplate.feignTarget();
            // String feignName = target.name();           
            // if ("oneFeign".equals(feignName)) {
            //        template.header("Authorization", headerValue);
            // }  else if (!"twoFeign".equals(feignName)) {
            //       template.header("Authorization", headerValue);
            // }
           
        };
    }

}