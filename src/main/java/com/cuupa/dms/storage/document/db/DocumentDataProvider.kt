package com.cuupa.dms.storage.document.db

import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.function.SerializablePredicate
import java.util.*

class DocumentDataProvider(storageService: StorageService, username: String, processesForOwner: List<String>) : ListDataProvider<Document>(getDocuments(storageService, username, processesForOwner)) {
    private var filtertext: String? = null

    constructor(storageService: StorageService, username: String) : this(storageService, username, listOf())

    var filter: String
        get() = super.getFilter().toString()
        set(filtertext) {
            Objects.requireNonNull(filtertext, "Must not be null")
            val finalFiltertext = filtertext.trim { it <= ' ' }.toLowerCase()
            if (this.filtertext == finalFiltertext) {
                return
            }
            this.filtertext = finalFiltertext
            setFilter { (_, name, sender, _, _, tags) ->
                passesFilter(name, filtertext) ||
                        passesFilter(tags
                                .map { (name1) -> name1.toLowerCase() }
                                , filtertext) ||
                        passesFilter(sender, filtertext)
            }
        }

    fun setFilter(localFilter: Set<String>?) {
        if (localFilter != null) {
            filter = SerializablePredicate<Document> { (_, _, _, _, _, tags) ->
                passesFilter(tags
                        .map { (name) -> name.toLowerCase() }
                        , filter)
            }.toString()
        }
    }

    private fun passesFilter(tags: List<String>?, filter: Set<String>): Boolean {
        return tags != null && filter.isNotEmpty() && filter.stream().anyMatch(tags::contains)
    }

    private fun passesFilter(name: String?, filtertext: String): Boolean {
        return name != null && name.toLowerCase().contains(filtertext)
    }

    private fun passesFilter(values: List<String>, filtertext: String): Boolean {
        return values.stream().anyMatch { value: String -> value.contains(filtertext) }
    }
}

private fun getDocuments(storageService: StorageService, username: String, processesForOwner: List<String>): List<Document> {
    return if (processesForOwner.isNullOrEmpty()) {
        storageService.findDocumentsByOwner(username)
    } else {
        storageService.findDocumentsByOwnerAndProcessInstanceId(username, processesForOwner)
    }
}

