package com.cuupa.dms.ui.overview

import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.ui.PropertyLayout
import org.springframework.beans.factory.annotation.Autowired

class DocumentPropertiesLayout(@Autowired val accessControl: AccessControl) : PropertyLayout() {

    override fun setDocument(document: Document, tags: List<Tag>) {
        filename.value = document.name
        sender.value = document.sender
        date.value = document.createDate.toLocalDate()
        time.value = document.createDate.toLocalTime()
        this.tags.setItems(tags)
        //this.tags.updateSelection(new HashSet<>(document.getTags()), new HashSet<>());
//this.tags.setValue();
    }

    init {
        if (accessControl.isUserSingedIn) {
            buildGui()
        }
    }
}