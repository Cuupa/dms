package com.cuupa.dms.ui.fileupload

import com.cuupa.dms.service.extern.Document
import com.cuupa.dms.service.extern.ExternConverterService
import com.cuupa.dms.service.extern.ExternSemanticService
import com.cuupa.dms.service.extern.SemanticResult
import com.cuupa.dms.ui.documentviews.DocumentView
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.upload.SucceededEvent
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime

class FileUploadSucceededListener(private val externSemanticService: ExternSemanticService,
                                  private val externConverterService: ExternConverterService, private val buffer:
                                  MultiFileMemoryBuffer, private val properties: MutableList<FileUploadProperties>,
                                  private val preview:
                                  MutableList<DocumentView>) : ComponentEventListener<SucceededEvent> {

    override fun onComponentEvent(event: SucceededEvent) {
        val documentContent = IOUtils.toByteArray(buffer.getInputStream(event.fileName))

        val convertedDocument = externConverterService.convert(Document(event.fileName, documentContent, false))
        val semanticResults = getResultFromSemantic(externSemanticService, convertedDocument.content)

        val fileUploadProperties = createFileUploadProperties(event, semanticResults)

        val documentView = DocumentView().get(event.fileName, StreamResource(event.fileName, InputStreamFactory {
            ByteArrayInputStream(buffer.getInputStream(event.fileName).readAllBytes())
        }))
        preview.add(documentView)
        properties.add(fileUploadProperties)
    }

    private fun createFileUploadProperties(event: SucceededEvent,
                                           semanticResults: List<SemanticResult>): FileUploadProperties {
        val fileUploadProperties = FileUploadProperties()
        fileUploadProperties.setFilename(event.fileName)

        if (semanticResults.isEmpty()) {
            setDate(fileUploadProperties)
        } else {
            setSemanticDetails(fileUploadProperties, semanticResults)
        }

        fileUploadProperties.content = getBytes(buffer, event)
        return fileUploadProperties
    }

    private fun setSemanticDetails(fileUploadProperties: FileUploadProperties, semanticResults: List<SemanticResult>) {
        fileUploadProperties.setFrom(semanticResults[0].sender)
        setDate(fileUploadProperties)
        fileUploadProperties.setTags(semanticResults.map(SemanticResult::topicName))
        val dueDate = getDueDate(semanticResults).stream().findFirst()
        if (dueDate.isPresent) {
            fileUploadProperties.setDueDate(dueDate.get())
        } else {
            fileUploadProperties.disableDueDate()
        }
    }

    private fun setDate(fileUploadProperties: FileUploadProperties) {
        fileUploadProperties.setDate(LocalDate.now())
        fileUploadProperties.setTime(LocalTime.now())
    }

    private fun getDueDate(result: List<SemanticResult>): List<String> {
        return result.map { localResult ->
            localResult.metaData.filter { metadata -> metadata.name == "dueDate" }.first()
        }.map { it.value }
    }

    private fun getBytes(buffer: MultiFileMemoryBuffer, event: SucceededEvent): ByteArray {
        try {
            ByteArrayInputStream(buffer.getInputStream(event.fileName)
                    .readAllBytes()).use { byteArrayInputStream -> return byteArrayInputStream.readAllBytes() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ByteArray(0)
    }

    private fun getResultFromSemantic(externSemanticService: ExternSemanticService,
                                      value: ByteArray): List<SemanticResult> {
        try {
            return externSemanticService.analyze(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

}