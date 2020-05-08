package com.cuupa.dms.configuration

import com.cuupa.dms.service.extern.ExternConverterService
import com.cuupa.dms.service.extern.ExternSemanticService
import com.cuupa.dms.service.extern.SemanticResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Type
import java.util.*

@Configuration
open class ExternalConfiguration {

    @Autowired
    private val encryptionConfiguration: EncryptionConfiguration? = null

    @Bean
    open fun externSemanticService(): ExternSemanticService {
        return ExternSemanticService(restTemplate(), gson(), listType())
    }

    @Bean
    open fun externConverterService(): ExternConverterService {
        return ExternConverterService(restTemplate(), gson(), encryptionConfiguration!!.base64Decoder())
    }

    @Bean
    open fun gson(): Gson {
        return Gson()
    }

    @Bean
    open fun listType(): Type {
        return object : TypeToken<ArrayList<SemanticResult?>?>() {}.type
    }

    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}