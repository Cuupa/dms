package com.cuupa.dms.ui.fileupload

import com.cuupa.dms.service.extern.ExternSemanticService
import com.cuupa.dms.service.extern.SemanticResult
import com.cuupa.dms.ui.documentviews.PdfView
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.upload.SucceededEvent
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalTime

class FileUploadSucceededListener(private val externSemanticService: ExternSemanticService, private val buffer: MultiFileMemoryBuffer, private val properties: MutableList<FileUploadProperties>, private val preview: MutableList<PdfView>) : ComponentEventListener<SucceededEvent> {

    override fun onComponentEvent(event: SucceededEvent) {
        val fileUploadProperties = FileUploadProperties()
        fileUploadProperties.setFilename(event.fileName)

        val semanticResults = getResultFromSemantic(externSemanticService, buffer.getInputStream(event.fileName))

        if (semanticResults.isEmpty()) {
            fileUploadProperties.setDate(LocalDate.now())
            fileUploadProperties.setTime(LocalTime.now())
        } else {
            fileUploadProperties.setFrom(semanticResults[0].sender)
            fileUploadProperties.setDate(LocalDate.now())
            fileUploadProperties.setTime(LocalTime.now())
            fileUploadProperties.setTags(semanticResults.map(SemanticResult::topicName))
        }

        fileUploadProperties.content = getBytes(buffer, event)

        val pdfView = PdfView(StreamResource(event.fileName, InputStreamFactory {
            ByteArrayInputStream(buffer.getInputStream(event.fileName).readAllBytes())
        }))
        preview.add(pdfView)
        properties.add(fileUploadProperties)
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

    private fun getResultFromSemantic(externSemanticService: ExternSemanticService, inputStream: InputStream): List<SemanticResult> {
        try {
            val value = IOUtils.toByteArray(inputStream)
            return externSemanticService.analize(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

}