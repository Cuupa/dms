package com.cuupa.dms.configuration

import com.cuupa.dms.service.extern.ExternSemanticService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ExternalConfiguration {
    @Bean
    open fun externSemanticService(): ExternSemanticService {
        return ExternSemanticService()
    }
}