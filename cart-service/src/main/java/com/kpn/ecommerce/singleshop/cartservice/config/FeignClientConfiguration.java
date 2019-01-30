package com.kpn.ecommerce.singleshop.cartservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Client;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class  FeignClientConfiguration {

    //This configuration disables the Ribbon client side load balancing
    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }

    //This bean makes sure that we can deserialize objects retrieved through feign client methods
    @Bean
    public Decoder feignDecoder() {
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
