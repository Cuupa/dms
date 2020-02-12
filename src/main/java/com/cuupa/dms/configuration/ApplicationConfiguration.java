package com.cuupa.dms.configuration;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.authentication.DatabaseAccessControl;
import com.cuupa.dms.controller.UploadController;
import com.cuupa.dms.controller.UploadValidator;
import com.cuupa.dms.database.user.UserRepository;
import com.cuupa.dms.service.EncryptionService;
import com.cuupa.dms.service.MailService;
import com.cuupa.dms.service.extern.ExternSemanticService;
import com.cuupa.dms.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({StorageConfiguration.class, ExternalConfiguration.class})
public class ApplicationConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ExternSemanticService externSemanticService;

    @Bean
    public EncryptionService passwordEncryptionService() {
        return new EncryptionService();
    }

    @Bean
    public AccessControl accessControl() {
        return new DatabaseAccessControl(userRepository, passwordEncryptionService());
    }

    @Bean
    public UploadController uploadController() {
        return new UploadController(storageService, externSemanticService, uploadValidator());
    }

    @Bean
    public UploadValidator uploadValidator() {
        return new UploadValidator(accessControl());
    }

    @Bean
    public MailService mailService() {
        return new MailService();
    }
}


