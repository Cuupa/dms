package com.cuupa.dms.storage.document.db

import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag

object DocumentMapper {

    fun mapToEntity(document: Document): DBDocument {
        return DBDocument(document.filename,
                document.name,
                document.sender,
                document.owner,
                document.createDate,
                document.tags.map(Tag::name), document.processInstanceId)
    }

    fun mapToGuiObject(document: DBDocument): Document {
        return Document(document.filename,
                document.name,
                document.sender,
                document.owner,
                document.createDate,
                document.tags
                        .map { tag: String? -> Tag(tag!!, document.owner) },
                document.processInstanceId
        )
    }
}