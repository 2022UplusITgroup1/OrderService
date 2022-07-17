package com.uplus.orderservice.feginclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.uplus.orderservice.feginclient.SearchApiInfo;

import feign.RequestInterceptor;
import feign.Target;
import feign.template.Template;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Target target = requestTemplate.feignTarget();
            String feignName = target.name();           
            if ("searchcorrect".equals(feignName)) {
                requestTemplate.header(SearchApiInfo.NAVERCLIENTIDHEADER, SearchApiInfo.CLIENTID);
                requestTemplate.header(SearchApiInfo.NAVERCLIENTSECRETHEADER, SearchApiInfo.CLIENTSECRET);
            } 
           
        };
    }

}