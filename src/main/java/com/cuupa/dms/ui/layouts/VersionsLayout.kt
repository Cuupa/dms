package com.cuupa.dms.ui.layouts

import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.vaadin.flow.component.listbox.ListBox
import com.vaadin.flow.component.orderedlayout.VerticalLayout

class VersionsLayout(private val storageService: StorageService) : VerticalLayout() {

    private lateinit var document: Document

    fun setDocument(document: Document) {
        this.document = document
        val documents = storageService.findDocumentsByName(document.name)
        listBox.setItems(documents.map { it.revision })
        listBox.value = documents.map { it.revision }.max()
    }

    private val listBox = ListBox<Int>()

    init {
        setSizeFull()
        listBox.setSizeFull()
        add(listBox)
    }

}
