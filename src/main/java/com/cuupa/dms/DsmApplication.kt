package com.cuupa.dms;

import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class DsmApplication {

    @Autowired
    private StorageService storage;

    public static void main(String[] args) {
        SpringApplication.run(DsmApplication.class, args);
    }

    @PostConstruct
    public void setup() {
        storage.deleteAll();
        Document
                document =
                new Document("",
                             "filename.pdf",
                             "company 1",
                             "user",
                             LocalDateTime.now(),
                             Arrays.asList(new Tag("Bill"), new Tag("Invoice")));
        Document
                document2 =
                new Document("",
                             "filename2.pdf",
                             "company 2",
                             "user",
                             LocalDateTime.now(),
                             Arrays.asList(new Tag("Bill"), new Tag("Invoice")));
        Document
                document3 =
                new Document("",
                             "filename3.pdf",
                             "company 2",
                             "a",
                             LocalDateTime.now(),
                             Arrays.asList(new Tag("Bill"), new Tag("Invoice")));
        storage.save(document);
        storage.save(document2);
        storage.save(document3);
    }

}
