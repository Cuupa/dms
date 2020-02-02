package com.cuupa.dms.configuration;

import com.cuupa.dms.authentication.SimpleUserAccessControl;
import com.cuupa.dms.authentication.UserAccessControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({StorageConfiguration.class, ExternalConfiguration.class})
public class ApplicationConfiguration {

    @Bean
    public UserAccessControl getUserAccessControl() {
        return new SimpleUserAccessControl();
    }

}
