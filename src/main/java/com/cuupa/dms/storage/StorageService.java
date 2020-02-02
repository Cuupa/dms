package com.cuupa.dms.storage;

import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.document.db.DocumentMapper;
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage;
import com.cuupa.dms.storage.tag.Tag;
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
        document.setOwner(document.getOwner().trim());
        tags.forEach(tag -> tag.setOwner(document.getOwner()));
        tags.forEach(tag -> tag.setName(tag.getName().trim()));
        List<Tag> tagsInDB = getTagsFromDB(tags);

        saveNewTags(tags, tagsInDB);
        tagsInDB = getTagsFromDB(tags);
        com.cuupa.dms.storage.document.db.Document documentToSave = DocumentMapper.mapToEntity(document);

        setTagsToDocument(tags, tagsInDB, documentToSave);
        documentStorage.save(documentToSave);
    }

    private List<Tag> getTagsFromDB(List<Tag> tags) {
        return tags.stream()
                   .filter(tag -> tagStorage.findTagByNameAndOwner(tag.getName(), tag.getOwner()) != null)
                   .collect(Collectors.toList());
    }

    private void setTagsToDocument(List<Tag> tags, List<Tag> tagsInDB, com.cuupa.dms.storage.document.db.Document documentToSave) {
        for (Tag tag : tags) {
            for (Tag tagFromDB : tagsInDB) {
                if (tag.equals(tagFromDB)) {
                    documentToSave.getTags().add(tagFromDB.getId());
                }
            }
        }
    }

    private void saveNewTags(List<Tag> tags, List<Tag> tagsInDB) {
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

        filteredTags.forEach(tagStorage::save);
    }

    public List<Document> findDocumentsByOwner(String owner) {
        if (StringUtils.isBlank(owner)) {
            return new ArrayList<>();
        }
        List<com.cuupa.dms.storage.document.db.Document> documentsByOwner = documentStorage.findDocumentsByOwner(owner);
        List<Long>
                tagIds =
                documentsByOwner.stream().flatMap(document -> document.getTags().stream()).collect(Collectors.toList());

        List<Tag>
                tagList =
                tagIds.stream()
                      .map(tagStorage::findTagById)
                      .filter(Objects::nonNull)
                      .map(tagFromDB -> new Tag(tagFromDB.get_id(), tagFromDB.getName(), tagFromDB.getOwner()))
                      .collect(Collectors.toList());

        List<Document> documents = new ArrayList<>(documentsByOwner.size());

        for (com.cuupa.dms.storage.document.db.Document document : documentsByOwner) {
            List<Long> tags = document.getTags();

            List<Tag> collect = tagList.stream().filter(tag -> tags.contains(tag.getId())).collect(Collectors.toList());
            documents.add(DocumentMapper.mapToGuiObject(document, collect));
        }

        return documents;
    }

    public List<Tag> findTagsByOwner(String owner) {
        return tagStorage.findTagsByOwner(owner)
                         .stream()
                         .map(tag -> new Tag(tag.getId(), tag.getName(), tag.getOwner()))
                         .collect(Collectors.toList());
    }
}
