package com.cuupa.dms.storage

import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.document.db.DBDocument
import com.cuupa.dms.storage.document.db.DocumentMapper
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.storage.tag.db.DBTag
import com.cuupa.dms.storage.tag.db.MongoDBTagStorage
import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.stream.Collectors

class StorageService(private val documentStorage: MongoDBDocumentStorage, private val tagStorage: MongoDBTagStorage) {

    fun deleteAll() {
        documentStorage.deleteAll()
        tagStorage.deleteAll()
    }

    fun save(document: Document) {
        val tags = document.tags
        val tagsWithOwner = tags.map { tag: Tag -> tag.copy(owner = tag.owner) }
        var tagsInDB = getTagsFromDB(tagsWithOwner)
        saveNewTags(tags, tagsInDB)
        document.tags = tags
        val documentToSave = DocumentMapper.mapToEntity(document)
        documentStorage.insert(documentToSave)
    }

    private fun getTagsFromDB(tags: List<Tag>): List<DBTag?> {
        return tags.stream()
                .map { tag: Tag -> tagStorage.findTagByNameAndOwner(tag.name, tag.owner) }
                .filter { obj: DBTag? -> Objects.nonNull(obj) }
                .collect(Collectors.toList())
    }

    private fun saveNewTags(tags: List<Tag>, tagsInDB: List<DBTag?>) {
        val filteredTags = tags.stream()
                .filter { tag: Tag ->
                    !tagsInDB.stream()
                            .map { tag.name }
                            .collect(Collectors.toList())
                            .contains(tag.name)
                }
                .filter { tag: Tag ->
                    !tagsInDB.stream()
                            .map { tag.owner }
                            .collect(Collectors.toList())
                            .contains(tag.owner)
                }
                .collect(Collectors.toList())
        val collect = filteredTags.stream().map { tag: Tag -> DBTag(tag.name, tag.owner) }.collect(Collectors.toList())
        for (tag in collect) {
            tagStorage.insert(tag)
        }
    }

    fun findDocumentsByOwner(owner: String?): List<Document> {
        return if (StringUtils.isBlank(owner)) {
            ArrayList()
        } else documentStorage.findDocumentsByOwner(owner).filterNotNull()
                .stream()
                .map { document: DBDocument? -> DocumentMapper.mapToGuiObject(document!!) }
                .collect(Collectors.toList())
    }

    fun findTagsByOwner(owner: String): List<Tag> {
        return tagStorage.findTagsByOwner(owner)
                .stream()
                .map { tag: DBTag -> Tag(tag.name, tag.owner) }
                .collect(Collectors.toList())
    }

}