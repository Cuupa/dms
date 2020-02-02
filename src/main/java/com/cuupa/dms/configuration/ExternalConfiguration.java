package com.cuupa.dms.configuration;

import com.cuupa.dms.service.extern.ExternSemanticService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalConfiguration {

    @Bean
    public ExternSemanticService externSemanticService() {
        return new ExternSemanticService();
    }
}
