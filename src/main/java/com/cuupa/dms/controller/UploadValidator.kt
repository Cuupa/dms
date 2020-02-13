package com.cuupa.dms.controller;

import com.cuupa.dms.authentication.AccessControl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class UploadValidator {

    private final AccessControl accessControl;

    public UploadValidator(@Autowired AccessControl accessControl) {
        this.accessControl = accessControl;
    }

    public Optional<ResponseEntity<String>> validate(String accessToken, String username, String filename, byte[] content) {
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(username)) {
            return Optional.of(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden"));
        }

        if (!accessControl.signIn(username, accessToken)) {
            return Optional.of(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden"));
        }

        if (StringUtils.isBlank(filename)) {
            return Optional.of(ResponseEntity.status(HttpStatus.NO_CONTENT).body("No filename specified"));
        }

        if (content == null || content.length == 0) {
            return Optional.of(ResponseEntity.status(HttpStatus.NO_CONTENT).body("No document"));
        }
        return Optional.empty();
    }
}
