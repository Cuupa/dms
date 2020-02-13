package com.cuupa.dms.storage.document.db

import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import java.util.stream.Collectors

object DocumentMapper {

    fun mapToEntity(document: Document): DBDocument {
        return DBDocument(document.filename,
                document.name,
                document.sender,
                document.owner,
                document.createDate,
                document.tags.stream().map(Tag::name).collect(Collectors.toList()))
    }

    fun mapToGuiObject(document: DBDocument): Document {
        return Document(document.filename,
                document.name,
                document.sender,
                document.owner,
                document.createDate,
                document.tags
                        .stream()
                        .map { tag: String? -> Tag(tag!!, document.owner) }
                        .collect(Collectors.toList()))
    }
}