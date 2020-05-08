package com.cuupa.dms.configuration

import com.cuupa.dms.service.EncryptionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
open class EncryptionConfiguration {

    @Bean
    open fun base64Decoder(): Base64.Decoder {
        return Base64.getDecoder()
    }

    @Bean
    open fun base64Encoder(): Base64.Encoder {
        return Base64.getEncoder()
    }

    @Bean
    open fun passwordEncryptionService(): EncryptionService {
        return EncryptionService(base64Decoder(), base64Encoder())
    }
}
