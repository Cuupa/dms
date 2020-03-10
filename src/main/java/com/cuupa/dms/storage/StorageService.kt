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

class StorageService(private val documentStorage: MongoDBDocumentStorage, private val tagStorage: MongoDBTagStorage,
                     private val camundaService: CamundaService) {

    fun deleteAll() {
        documentStorage.deleteAll()
        tagStorage.deleteAll()
    }

    fun save(document: Document): DBDocument {
        val tags = document.tags
        val tagsWithOwner = tags.map { tag: Tag -> tag.copy(owner = tag.owner) }
        val tagsInDB = getTagsFromDB(tagsWithOwner)
        saveNewTags(tags, tagsInDB)
        document.tags = tags

        val maxDocumentRevision = documentStorage.findDBDocumentByName(document.name).maxBy { it.revision }
        val documentToSave = DocumentMapper.mapToEntity(document)

        if (maxDocumentRevision != null) {
            documentToSave.revision = maxDocumentRevision.revision.inc()
        } else {
            documentToSave.revision = 1
        }
        return documentStorage.insert(documentToSave)
    }

    private fun getTagsFromDB(tags: List<Tag>): List<DBTag?> {
        return tags
                .map { tag: Tag -> tagStorage.findTagByNameAndOwner(tag.name, tag.owner) }
                .filter { obj: DBTag? -> Objects.nonNull(obj) }
    }

    private fun saveNewTags(tags: List<Tag>, tagsInDB: List<DBTag?>) {
        val filteredTags = tags.stream()
                .filter { tag: Tag ->
                    !tagsInDB.map { tag.name }
                            .contains(tag.name)
                }
                .filter { tag: Tag ->
                    !tagsInDB.map { tag.owner }
                            .contains(tag.owner)
                }
                .collect(Collectors.toList())
        val collect = filteredTags.map { tag: Tag -> DBTag(tag.name, tag.owner) }
        for (tag in collect) {
            tagStorage.insert(tag)
        }
    }

    fun findDocumentsByOwner(owner: String?): List<Document> {
        return if (owner.isNullOrBlank()) {
            listOf()
        } else findRevision(documentStorage.findDocumentsByOwnerOrderByName(owner).filterNotNull())
    }

    fun findDocumentsByOwnerAndProcessInstanceId(owner: String?, processInstanceIds: List<String>): List<Document> {
        return if (owner.isNullOrBlank()) {
            listOf()
        } else {
            findRevision(processInstanceIds.map { processInstanceId ->
                documentStorage
                        .findDBDocumentsByOwnerAndAndProcessInstanceId(owner, processInstanceId)
            }.filterNotNull())
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

    private fun findRevision(documents: List<DBDocument>): List<Document> {
        var lastDocument: DBDocument? = null
        val resultListFromDb: MutableList<DBDocument> = mutableListOf()
        for (document in documents) {
            if (lastDocument == null) {
                lastDocument = document
            }
            if (lastDocument.name == document.name) {
                if (document.revision > lastDocument.revision) {
                    lastDocument = document
                }
            } else {
                resultListFromDb.add(lastDocument)
                lastDocument = document
            }
        }
        if (!resultListFromDb.contains(lastDocument) && lastDocument != null) {
            resultListFromDb.add(lastDocument)
        }
        return resultListFromDb.map { document: DBDocument -> DocumentMapper.mapToGuiObject(document) }

    }
}