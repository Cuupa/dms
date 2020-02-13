package com.cuupa.dms.controller

import com.cuupa.dms.service.extern.ExternSemanticService
import com.cuupa.dms.service.extern.SemanticResult
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.stream.Collectors

@RestController
class UploadController(@param:Autowired private val storageService: StorageService?, @param:Autowired private val externSemanticService: ExternSemanticService?, @param:Autowired private val uploadValidator: UploadValidator) {

    @RequestMapping(value = ["/api/rest/1.0/upload"], method = [RequestMethod.POST])
    fun uploadDocument(@RequestBody accessToken: String?, @RequestBody username: String?, @RequestBody filename: String?, @RequestBody content: ByteArray?): ResponseEntity<String> {
        val optionalResponse = uploadValidator.validate(accessToken, username, filename, content)
        if (optionalResponse.isPresent) {
            return optionalResponse.get()
        }
        val result = externSemanticService!!.analize(content!!)
        val senderString = getSender(result)
        val tags = result.stream().map(SemanticResult::topicName).map { name: String? -> Tag(name!!) }.collect(Collectors.toList())
        val document = Document(filename!!, filename, senderString!!, username!!, LocalDateTime.now(), tags)
        storageService!!.save(document)
        return ResponseEntity.status(HttpStatus.CREATED).body(document.name)
    }

    private fun getSender(result: List<SemanticResult>): String? {
        val sender = result.stream().filter { e: SemanticResult -> StringUtils.isNotEmpty(e.sender) }.findFirst()
        var senderString: String? = null
        if (sender.isPresent) {
            senderString = sender.get().sender
        }
        return senderString
    }

}