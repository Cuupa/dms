package com.cuupa.dms.storage.tag.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class TagModelListener extends AbstractMongoEventListener<com.cuupa.dms.storage.tag.Tag> {

    private static final Log LOGGER = LogFactory.getLog(TagModelListener.class);

    private TagSequenceGenerator tagSequenceGenerator;

    public TagModelListener(@Autowired TagSequenceGenerator sequenceGenerator) {
        this.tagSequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<com.cuupa.dms.storage.tag.Tag> event) {
        LOGGER.debug("called onBeforeConvert");
        if (event.getSource().getId() < 1) {
            event.getSource().setId(tagSequenceGenerator.generateSequence(Tag.SEQUENCE_NAME));
        }
    }
}
