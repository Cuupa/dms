package com.cuupa.dms.configuration

import CamundaInitListener
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.authentication.DatabaseAccessControl
import com.cuupa.dms.controller.UploadValidator
import com.cuupa.dms.database.user.UserRepository
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.service.EncryptionService
import com.cuupa.dms.service.MailService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(StorageConfiguration::class, ExternalConfiguration::class, CamundaConfiguration::class, DelegateConfiguration::class)
open class ApplicationConfiguration {

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val runtimeService: RuntimeService? = null

    @Autowired
    private val taskService: TaskService? = null

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

    @Bean
    open fun camundaService(): CamundaService {
        return CamundaService(runtimeService!!, taskService!!)
    }
}