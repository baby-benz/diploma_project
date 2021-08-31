package com.itmo.accounting.configuration;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GensonConfig {
    @Bean
    public Genson genson() {
        return new GensonBuilder().withConverters(new LocalDateConverter(), new StringConverter()).create();
    }
}
