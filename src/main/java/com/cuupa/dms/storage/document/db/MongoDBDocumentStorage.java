package com.cuupa.dms.storage.document.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoDBDocumentStorage extends MongoRepository<DBDocument, Long> {

    List<DBDocument> findDocumentsByOwner(String owner);
}
