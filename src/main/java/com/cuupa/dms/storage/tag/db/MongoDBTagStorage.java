package com.cuupa.dms.storage.tag.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoDBTagStorage extends MongoRepository<DBTag, Long> {

    List<DBTag> findTagsByOwner(String owner);

    //DBTag findTagById(long id);

    DBTag findTagByNameAndOwner(String name, String owner);
}
