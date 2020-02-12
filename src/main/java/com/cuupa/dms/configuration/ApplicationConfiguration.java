package com.cuupa.dms.configuration;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.authentication.DatabaseAccessControl;
import com.cuupa.dms.database.user.UserRepository;
import com.cuupa.dms.service.EncryptionService;
import com.cuupa.dms.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({StorageConfiguration.class, ExternalConfiguration.class})
public class ApplicationConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public AccessControl accessControl() {
        return new DatabaseAccessControl(userRepository, passwordEncryptionService());
    }

    @Bean
    public EncryptionService passwordEncryptionService() {
        return new EncryptionService();
    }

    @Bean
    public MailService mailService() {
        return new MailService();
    }
}


