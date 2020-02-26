package com.cuupa.dms.storage.tag

import com.cuupa.dms.storage.tag.db.DBTag

data class Tag(var name: String, var owner: String?) : Comparable<Tag> {

    constructor(name: String) : this(name, null)

    override fun toString(): String {
        return name
    }

    override fun compareTo(other: Tag): Int {
        return name.compareTo(other.name)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Tag) {
            return other.name == name
        }
        if (other is DBTag) {
            return other.name == name
        }
        return false
    }
}