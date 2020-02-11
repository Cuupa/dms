package com.cuupa.dms.configuration;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.authentication.DatabaseAccessControl;
import com.cuupa.dms.database.user.UserRepository;
import com.cuupa.dms.service.PasswordEncryptionService;
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
    public PasswordEncryptionService passwordEncryptionService() {
        return new PasswordEncryptionService();
    }
}


