package com.cuupa.dms.storage.tag.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoDBTagStorage extends MongoRepository<com.cuupa.dms.storage.tag.Tag, Long> {

    List<com.cuupa.dms.storage.tag.Tag> findTagsByOwner(String owner);

    Tag findTagById(long id);

    Tag findTagByNameAndOwner(String name, String owner);
}
