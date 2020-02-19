package com.cuupa.dms.configuration

import CamundaInitListener
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.authentication.DatabaseAccessControl
import com.cuupa.dms.controller.UploadValidator
import com.cuupa.dms.database.user.UserRepository
import com.cuupa.dms.service.EncryptionService
import com.cuupa.dms.service.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(StorageConfiguration::class, ExternalConfiguration::class, CamundaConfiguration::class, DelegateConfiguration::class)
open class ApplicationConfiguration {

    @Autowired
    private val userRepository: UserRepository? = null

    @Bean
    open fun passwordEncryptionService(): EncryptionService {
        return EncryptionService()
    }

    @Bean
    open fun accessControl(): AccessControl {
        return DatabaseAccessControl(userRepository!!, passwordEncryptionService())
    }

    @Bean
    open fun uploadValidator(): UploadValidator {
        return UploadValidator(accessControl())
    }

    @Bean
    open fun mailService(): MailService {
        return MailService()
    }

    @Bean
    open fun camundaInitListener(): CamundaInitListener {
        return CamundaInitListener()
    }


}