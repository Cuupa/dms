package com.cuupa.dms.configuration;

import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.db.DocumentModelListener;
import com.cuupa.dms.storage.document.db.DocumentSequenceGenerator;
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage;
import com.cuupa.dms.storage.tag.db.MongoDBTagStorage;
import com.cuupa.dms.storage.tag.db.TagModelListener;
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
    public DocumentSequenceGenerator documentSequenceGenerator() {
        return new DocumentSequenceGenerator();
    }

    @Bean
    public DocumentModelListener documentModelListener() {
        return new DocumentModelListener(documentSequenceGenerator());
    }

    @Bean
    public TagSequenceGenerator tagSequenceGenerator() {
        return new TagSequenceGenerator();
    }

    @Bean
    public TagModelListener tagModelListener() {
        return new TagModelListener(tagSequenceGenerator());
    }
}
