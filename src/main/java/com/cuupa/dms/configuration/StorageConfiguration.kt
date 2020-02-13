package com.cuupa.dms.configuration

import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage
import com.cuupa.dms.storage.tag.db.MongoDBTagStorage
import com.cuupa.dms.storage.tag.db.TagSequenceGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class StorageConfiguration {
    @Autowired
    private val documentStorage: MongoDBDocumentStorage? = null
    @Autowired
    private val tagStorage: MongoDBTagStorage? = null

    @Bean
    open fun storageService(): StorageService {
        return StorageService(documentStorage!!, tagStorage!!)
    }

    @Bean
    open fun tagSequenceGenerator(): TagSequenceGenerator {
        return TagSequenceGenerator()
    }
}