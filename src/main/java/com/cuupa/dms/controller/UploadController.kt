package com.cuupa.dms.controller

import com.cuupa.dms.service.extern.ExternConverterService
import com.cuupa.dms.service.extern.ExternSemanticService
import com.cuupa.dms.service.extern.SemanticResult
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.ui.fileupload.DocumentSaveService
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class UploadController(@param:Autowired private val documentSaveService: DocumentSaveService, @param:Autowired private
val externConverterService: ExternConverterService,
                       @param:Autowired
                       private val externSemanticService:
                       ExternSemanticService, @param:Autowired private val uploadValidator: UploadValidator) {

    @RequestMapping(value = ["/api/rest/1.0/upload"], method = [RequestMethod.POST])
    fun uploadDocument(@RequestBody accessToken: String?, @RequestBody username: String?,
                       @RequestBody filename: String?, @RequestBody content: ByteArray?): ResponseEntity<String> {
        val errorResponse = uploadValidator.validate(accessToken, username, filename, content)
        if (errorResponse.isPresent) {
            return errorResponse.get()
        }
        val convertedDocument = externConverterService.convert(com.cuupa.dms.service.extern.Document(filename!!, content!!, false))
        val result = externSemanticService.analyze(convertedDocument.content)

        val tags = result.map(SemanticResult::topicName).map { name: String? -> Tag(name!!) }

        val document = Document(filename, filename, getSender(result)!!, username!!, LocalDateTime.now(), tags,
                processInstanceId = null, revision = -1)

        documentSaveService.save(document, convertedDocument.content, dueDate = getDueDate(result).firstOrNull())
        return ResponseEntity.status(HttpStatus.CREATED).body(document.name)
    }

    private fun getDueDate(result: List<SemanticResult>): List<String> {
        return result.filter { localResult ->
            localResult.metaData.filter { metadata -> metadata.name == "dueDate" }.any()
        }.flatMap { metaDataList -> metaDataList.metaData.map { metadata -> metadata.value } }
    }

    private fun getSender(result: List<SemanticResult>): String? {
        return result.filter { e: SemanticResult -> StringUtils.isNotEmpty(e.sender) }.map(SemanticResult::sender)
                .firstOrNull()
    }

}