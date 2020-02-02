package com.cuupa.dms.storage.document.db;

import com.cuupa.dms.storage.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentMapper {

    public static Document mapToEntity(com.cuupa.dms.storage.document.Document document) {
        Document
                documentEntity =
                new Document(document.getId(),
                             document.getName(),
                             document.getSender(),
                             document.getOwner(),
                             document.getCreateDate(),
                             new ArrayList<>());
        documentEntity.setImage(document.getImage());
        return documentEntity;
    }

    public static com.cuupa.dms.storage.document.Document mapToGuiObject(Document document, List<Tag> tags) {
        com.cuupa.dms.storage.document.Document
                uiDocument =
                new com.cuupa.dms.storage.document.Document(document.getId(),
                                                            document.getName(),
                                                            document.getSender(),
                                                            document.getOwner(),
                                                            document.getCreateDate(),
                                                            tags.stream()
                                                                .map(tag -> new Tag(tag.getId(),
                                                                                    tag.getName(),
                                                                                    tag.getOwner()))
                                                                .collect(Collectors.toList()));
        uiDocument.setImage(document.getImage());
        return uiDocument;
    }
}
