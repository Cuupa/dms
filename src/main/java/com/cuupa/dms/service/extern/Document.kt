package com.cuupa.dms.service.extern

import java.io.Serializable
import java.util.*

data class Document(var filename: String, var content: ByteArray, var encrypted: Boolean) : Serializable {

    val contentBase64: String = Base64.getEncoder().encodeToString(content)

    constructor() : this("", ByteArray(0), false)

    constructor(filename: String, content: ByteArray) : this(filename, content, false)

    companion object {
        private const val serialVersionUID = 1L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (!content.contentEquals(other.content)) return false
        if (filename != other.filename) return false
        if (encrypted != other.encrypted) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.hashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + encrypted.hashCode()
        return result
    }
}