package com.cuupa.dms.controller

import com.cuupa.dms.authentication.AccessControl
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class UploadValidator(@param:Autowired private val accessControl: AccessControl) {

    fun validate(accessToken: String?, username: String?, filename: String?, content: ByteArray?): Optional<ResponseEntity<String>> {
        if (accessToken.isNullOrBlank() || username.isNullOrBlank()) {
            return Optional.of(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden"))
        }
        if (!accessControl.signIn(username, accessToken)) {
            return Optional.of(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden"))
        }
        if (StringUtils.isBlank(filename)) {
            return Optional.of(ResponseEntity.status(HttpStatus.NO_CONTENT).body("No filename specified"))
        }
        return if (content == null || content.isEmpty()) {
            Optional.of(ResponseEntity.status(HttpStatus.NO_CONTENT).body("No document"))
        } else Optional.empty()
    }

}