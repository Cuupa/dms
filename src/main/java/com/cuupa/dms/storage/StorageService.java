package com.cuupa.dms.storage;

import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.document.db.DBDocument;
import com.cuupa.dms.storage.document.db.DocumentMapper;
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage;
import com.cuupa.dms.storage.tag.Tag;
import com.cuupa.dms.storage.tag.db.DBTag;
import com.cuupa.dms.storage.tag.db.MongoDBTagStorage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StorageService {

    private final MongoDBDocumentStorage documentStorage;

    private final MongoDBTagStorage tagStorage;

    public StorageService(MongoDBDocumentStorage documentStorage, MongoDBTagStorage tagStorage) {
        this.documentStorage = documentStorage;
        this.tagStorage = tagStorage;
    }

    public void deleteAll() {
        documentStorage.deleteAll();
        tagStorage.deleteAll();
    }

    public void save(Document document) {
        List<Tag> tags = document.getTags();
        tags.forEach(tag -> tag.setOwner(document.getOwner()));
        tags.forEach(tag -> tag.setName(tag.getName().trim()));
        List<DBTag> tagsInDB = getTagsFromDB(tags);

        saveNewTags(tags, tagsInDB);
        tagsInDB = getTagsFromDB(tags);
        System.out.println(tagsInDB);
        document.setTags(tags);
        DBDocument documentToSave = DocumentMapper.mapToEntity(document);

        documentStorage.insert(documentToSave);
    }

    private List<DBTag> getTagsFromDB(List<Tag> tags) {
        return tags.stream()
                   .map(tag -> tagStorage.findTagByNameAndOwner(tag.getName(), tag.getOwner()))
                   .filter(Objects::nonNull)
                   .collect(Collectors.toList());
    }


    private void saveNewTags(List<Tag> tags, List<DBTag> tagsInDB) {
        List<Tag>
                filteredTags =
                tags.stream()
                    .filter(tag -> !tagsInDB.stream()
                                            .map(tag1 -> tag.getName())
                                            .collect(Collectors.toList())
                                            .contains(tag.getName()))
                    .filter(tag -> !tagsInDB.stream()
                                            .map(tag1 -> tag.getOwner())
                                            .collect(Collectors.toList())
                                            .contains(tag.getOwner()))
                    .collect(Collectors.toList());


        List<DBTag>
                collect =
                filteredTags.stream().map(tag -> new DBTag(tag.getName(), tag.getOwner())).collect(Collectors.toList());
        for (DBTag tag : collect) {
            tagStorage.insert(tag);
        }
    }

    public List<Document> findDocumentsByOwner(String owner) {
        if (StringUtils.isBlank(owner)) {
            return new ArrayList<>();
        }
        List<DBDocument> documentsByOwner = documentStorage.findDocumentsByOwner(owner);

        List<Document> documents = new ArrayList<>(documentsByOwner.size());

        for (DBDocument document : documentsByOwner) {
            documents.add(DocumentMapper.mapToGuiObject(document));
        }

        return documents;
    }

    public List<Tag> findTagsByOwner(String owner) {
        return tagStorage.findTagsByOwner(owner).stream().map(tag -> new Tag(tag.getName(), tag.getOwner()))
                         .collect(Collectors.toList());
    }
}
