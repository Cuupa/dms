package com.cuupa.dms.storage.document

import com.cuupa.dms.storage.tag.Tag
import java.time.LocalDateTime

data class Document(var filename: String, var name: String, var sender: String, var owner: String, var createDate: LocalDateTime, var tags: List<Tag>)