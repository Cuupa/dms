package com.cuupa.dms.storage.document.db

import java.time.LocalDateTime

data class DBDocument(var filename: String, var name: String, var sender: String, var owner: String, var createDate: LocalDateTime, var tags: List<String>)