package com.cuupa.dms.ui.archive

import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.ui.documentviews.DocumentView
import com.cuupa.dms.ui.layouts.PropertyLayout
import com.cuupa.dms.ui.layouts.PropertyTabs
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
    private val versionsLayout = VersionsLayout()
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
            verticalLayout.add(PropertyTabs(previewLayout, propertiesLayout, versionsLayout))
            verticalLayout.add(previewLayout)
            verticalLayout.add(propertiesLayout)
            add(verticalLayout)
        }
    }
}