package com.cuupa.dms.storage.document.db;

import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;

import java.util.stream.Collectors;

public class DocumentMapper {

    public static DBDocument mapToEntity(Document document) {
        DBDocument
                documentEntity =
                new DBDocument(document.getFilename(),
                               document.getName(),
                               document.getSender(),
                               document.getOwner(),
                               document.getCreateDate(),
                               document.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        return documentEntity;
    }

    public static Document mapToGuiObject(DBDocument document) {
        Document
                uiDocument =
                new com.cuupa.dms.storage.document.Document(document.getFilename(),
                                                            document.getName(),
                                                            document.getSender(),
                                                            document.getOwner(),
                                                            document.getCreateDate(),
                                                            document.getTags()
                                                                    .stream()
                                                                    .map(tag -> new Tag(tag, document.getOwner()))
                                                                    .collect(Collectors.toList()));
        return uiDocument;
    }
}
