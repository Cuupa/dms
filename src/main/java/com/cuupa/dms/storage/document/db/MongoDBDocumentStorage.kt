package com.cuupa.dms.storage.document.db

import org.springframework.data.mongodb.repository.MongoRepository

interface MongoDBDocumentStorage : MongoRepository<DBDocument?, Long?> {

    fun findDocumentsByOwner(owner: String?): List<DBDocument?>

    fun findDBDocumentsByOwnerAndAndProcessInstanceId(owner: String?, processInstanceId: String?): DBDocument

}