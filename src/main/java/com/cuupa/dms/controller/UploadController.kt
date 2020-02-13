package com.cuupa.dms.controller;

import com.cuupa.dms.service.extern.ExternSemanticService;
import com.cuupa.dms.service.extern.SemanticResult;
import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController("/api/rest/v1")
public class UploadController {


    private final StorageService storageService;

    private final ExternSemanticService externSemanticService;

    private final UploadValidator uploadValidator;

    public UploadController(@Autowired StorageService storageService, @Autowired ExternSemanticService externSemanticService, @Autowired UploadValidator uploadValidator) {
        this.storageService = storageService;
        this.externSemanticService = externSemanticService;
        this.uploadValidator = uploadValidator;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadDocument(@RequestBody final String accessToken, @RequestBody final String username, @RequestBody final String filename, @RequestBody final byte[] content) {
        Optional<ResponseEntity<String>>
                optionalResponse =
                uploadValidator.validate(accessToken, username, filename, content);
        if (optionalResponse.isPresent()) {
            return optionalResponse.get();
        }

        final List<SemanticResult> result = externSemanticService.analize(content);

        String senderString = getSender(result);

        final List<Tag>
                tags =
                result.stream().map(SemanticResult::getTopicName).map(Tag::new).collect(Collectors.toList());

        final Document document = new Document(filename, filename, senderString, username, LocalDateTime.now(), tags);
        storageService.save(document);

        return ResponseEntity.status(HttpStatus.CREATED).body(document.getName());
    }


    private String getSender(List<SemanticResult> result) {
        final Optional<SemanticResult>
                sender =
                result.stream().filter(e -> StringUtils.isNotEmpty(e.getSender())).findFirst();

        String senderString = null;
        if (sender.isPresent()) {
            senderString = sender.get().getSender();
        }
        return senderString;
    }
}
