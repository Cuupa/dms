package com.cuupa.dms.storage.tag.db

import org.springframework.data.mongodb.repository.MongoRepository

interface MongoDBTagStorage : MongoRepository<DBTag?, Long?> {

    fun findTagsByOwner(owner: String): List<DBTag>
    //DBTag findTagById(long id);
    fun findTagByNameAndOwner(name: String, owner: String?): DBTag?
}