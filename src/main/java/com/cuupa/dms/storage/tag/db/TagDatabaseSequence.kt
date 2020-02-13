package com.cuupa.dms.storage.tag.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tag_database_sequence")
class TagDatabaseSequence {
    @Id
    var id: String? = null
    var seq: Long = 0

}