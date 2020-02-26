package com.cuupa.dms.ui.overview

import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.ui.PropertyLayout
import com.cuupa.dms.ui.PropertyTabs
import com.cuupa.dms.ui.documentviews.DocumentView
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.springframework.beans.factory.annotation.Autowired

class DocumentPreview(@Autowired accessControl: AccessControl, private val documentGrid: Grid<Document>, private val
tagsByOwner: List<Tag>) :
        HorizontalLayout() {

    private val previewLayout = VerticalLayout()
    private var lastView: DocumentView = DocumentView()
    private val propertiesLayout: PropertyLayout = DocumentPropertiesLayout(accessControl)
    fun loadImage() {
        val document = documentGrid.asSingleSelect().value
        if (document != null) {
            val pdfView = DocumentView().get(document)
            previewLayout.replace(lastView, pdfView)
            lastView = pdfView
        }
    }

    fun loadProperties() {
        val document = documentGrid.asSingleSelect().value
        if (document != null) {
            propertiesLayout.setDocument(document, tagsByOwner)
        }
    }

    init {
        if (accessControl.isUserSingedIn) {
            setSizeFull()
            val verticalLayout = VerticalLayout()
            previewLayout.add(lastView)
            previewLayout.setSizeFull()
            propertiesLayout.isVisible = false
            verticalLayout.add(PropertyTabs(previewLayout, propertiesLayout))
            verticalLayout.add(previewLayout)
            verticalLayout.add(propertiesLayout)
            add(verticalLayout)
        }
    }
}