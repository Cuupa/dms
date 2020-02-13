package com.cuupa.dms.configuration

import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.authentication.DatabaseAccessControl
import com.cuupa.dms.controller.StatusController
import com.cuupa.dms.controller.UploadController
import com.cuupa.dms.controller.UploadValidator
import com.cuupa.dms.database.user.UserRepository
import com.cuupa.dms.service.EncryptionService
import com.cuupa.dms.service.MailService
import com.cuupa.dms.service.extern.ExternSemanticService
import com.cuupa.dms.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(StorageConfiguration::class, ExternalConfiguration::class)
open class ApplicationConfiguration {
    @Autowired
    private val userRepository: UserRepository? = null
    @Autowired
    private val storageService: StorageService? = null
    @Autowired
    private val externSemanticService: ExternSemanticService? = null

    @Bean
    open fun passwordEncryptionService(): EncryptionService {
        return EncryptionService()
    }

    @Bean
    open fun accessControl(): AccessControl {
        return DatabaseAccessControl(userRepository!!, passwordEncryptionService())
    }

    //@Bean
    fun uploadController(): UploadController {
        return UploadController(storageService, externSemanticService, uploadValidator())
    }

    //@Bean
    fun statusController(): StatusController {
        return StatusController()
    }

    @Bean
    open fun uploadValidator(): UploadValidator {
        return UploadValidator(accessControl())
    }

    @Bean
    open fun mailService(): MailService {
        return MailService()
    }
}