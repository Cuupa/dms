package com.cuupa.dms.storage.tag.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class TagSequenceGenerator {

    @Autowired
    private MongoOperations mongoOperations;

    public long generateSequence(String sequenceName) {
        TagDatabaseSequence
                counter =
                mongoOperations.findAndModify(query(where("_id").is(sequenceName)),
                                              new Update().inc("seq", 1),
                                              options().returnNew(true).upsert(true),
                                              TagDatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1L;
    }
}
