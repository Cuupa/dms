package com.cuupa.dms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @RequestMapping(value = "/api/rest/1.0/status", method = RequestMethod.GET)
    public ResponseEntity<String> status() {
        return ResponseEntity.status(HttpStatus.OK).body("Service up");
    }
}
