package com.cuupa.dms.configuration;

import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage;
import com.cuupa.dms.storage.tag.db.MongoDBTagStorage;
import com.cuupa.dms.storage.tag.db.TagSequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Autowired
    private MongoDBDocumentStorage documentStorage;

    @Autowired
    private MongoDBTagStorage tagStorage;

    @Bean
    public StorageService storageService() {
        return new StorageService(documentStorage, tagStorage);
    }

    @Bean
    public TagSequenceGenerator tagSequenceGenerator() {
        return new TagSequenceGenerator();
    }
}
