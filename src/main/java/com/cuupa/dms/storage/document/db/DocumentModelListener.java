package com.cuupa.dms.storage.document.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class DocumentModelListener extends AbstractMongoEventListener<Document> {

    private static final Log LOGGER = LogFactory.getLog(DocumentModelListener.class);

    private DocumentSequenceGenerator documentSequenceGenerator;

    public DocumentModelListener(@Autowired DocumentSequenceGenerator sequenceGeerator) {
        this.documentSequenceGenerator = sequenceGeerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Document> event) {
        LOGGER.debug("called onBeforeConvert");
        if (event.getSource().getId() < 1) {
            event.getSource().setId(documentSequenceGenerator.generateSequence(Document.SEQUENCE_NAME));
        }
    }
}
