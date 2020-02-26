package com.cuupa.dms.configuration

import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage
import com.cuupa.dms.storage.tag.db.MongoDBTagStorage
import com.cuupa.dms.storage.tag.db.TagSequenceGenerator
import com.cuupa.dms.ui.fileupload.DocumentSaveService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class StorageConfiguration {

    @Autowired
    private val documentStorage: MongoDBDocumentStorage? = null

    @Autowired
    private val tagStorage: MongoDBTagStorage? = null

    @Autowired
    private val runtimeService: RuntimeService? = null

    @Autowired
    private val taskService: TaskService? = null

    @Bean
    open fun documentSaveService(): DocumentSaveService {
        return DocumentSaveService(storageService(), camundaService())
    }

    @Bean
    open fun storageService(): StorageService {
        return StorageService(documentStorage!!, tagStorage!!, camundaService())
    }

    @Bean
    open fun camundaService(): CamundaService {
        return CamundaService(runtimeService!!, taskService!!)
    }

    @Bean
    open fun tagSequenceGenerator(): TagSequenceGenerator {
        return TagSequenceGenerator()
    }
}