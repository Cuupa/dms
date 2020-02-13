package com.cuupa.dms.storage.tag.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.*

class TagSequenceGenerator {
    @Autowired
    private val mongoOperations: MongoOperations? = null

    fun generateSequence(sequenceName: String?): Long {
        val counter = mongoOperations!!.findAndModify(Query.query(Criteria.where("_id").`is`(sequenceName)),
                Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                TagDatabaseSequence::class.java)
        return if (!Objects.isNull(counter)) counter.seq else 1L
    }
}