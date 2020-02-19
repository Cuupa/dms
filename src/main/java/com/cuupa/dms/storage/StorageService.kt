package com.cuupa.dms.storage

import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.document.db.DBDocument
import com.cuupa.dms.storage.document.db.DocumentMapper
import com.cuupa.dms.storage.document.db.MongoDBDocumentStorage
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.storage.tag.db.DBTag
import com.cuupa.dms.storage.tag.db.MongoDBTagStorage
import java.util.*
import java.util.stream.Collectors

class StorageService(private val documentStorage: MongoDBDocumentStorage, private val tagStorage: MongoDBTagStorage, private val camundaService: CamundaService) {

    fun deleteAll() {
        documentStorage.deleteAll()
        tagStorage.deleteAll()
    }

    fun save(document: Document): DBDocument {
        val tags = document.tags
        val tagsWithOwner = tags.map { tag: Tag -> tag.copy(owner = tag.owner) }
        var tagsInDB = getTagsFromDB(tagsWithOwner)
        saveNewTags(tags, tagsInDB)
        document.tags = tags
        val documentToSave = DocumentMapper.mapToEntity(document)
        return documentStorage.insert(documentToSave)
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
        return if (owner.isNullOrBlank()) {
            listOf()
        } else documentStorage.findDocumentsByOwner(owner).filterNotNull()
                .map { document: DBDocument? -> DocumentMapper.mapToGuiObject(document!!) }
    }

    fun findDocumentsByOwnerAndProcessInstanceId(owner: String?, processInstanceIds: List<String>): List<Document> {
        return if (owner.isNullOrBlank()) {
            listOf()
        } else {
            processInstanceIds.map { processInstanceId -> documentStorage.findDBDocumentsByOwnerAndAndProcessInstanceId(owner, processInstanceId) }.map { document: DBDocument? -> DocumentMapper.mapToGuiObject(document!!) }
        }
    }

    fun findTagsByOwner(owner: String): List<Tag> {
        return tagStorage.findTagsByOwner(owner)
                .stream()
                .map { tag: DBTag -> Tag(tag.name, tag.owner) }
                .collect(Collectors.toList())
    }

    fun complete(selectedDocument: Document?) {
        if (selectedDocument != null) {
            camundaService.complete(selectedDocument.processInstanceId)
            selectedDocument.processInstanceId = null
            save(selectedDocument)
        }
    }
}