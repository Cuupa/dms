package com.cuupa.dms.ui.inbox

import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.ui.documentviews.DocumentView
import com.cuupa.dms.ui.layouts.PropertyLayout
import com.cuupa.dms.ui.layouts.PropertyTabs
import com.cuupa.dms.ui.layouts.VersionsLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.springframework.beans.factory.annotation.Autowired

class InboxPreview(@Autowired val camundaService: CamundaService, @Autowired val storageService: StorageService,
                   @Autowired val accessControl: AccessControl,
                   private val documentGrid:
                   Grid<Document>,
                   private val tagsByOwner: List<Tag>) : HorizontalLayout() {

    private val previewLayout = VerticalLayout()
    private var lastView = DocumentView()
    private val propertiesLayout: PropertyLayout = InboxPropertiesLayout(camundaService, accessControl)
    private val versionsLayout = VersionsLayout(storageService)

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
            versionsLayout.setDocument(document)
        }
    }

    init {
        if (accessControl.isUserSingedIn) {
            setSizeFull()
            val verticalLayout = VerticalLayout()
            previewLayout.add(lastView)
            previewLayout.setSizeFull()
            propertiesLayout.isVisible = false
            versionsLayout.isVisible = false
            verticalLayout.add(PropertyTabs(previewLayout, propertiesLayout, versionsLayout))
            verticalLayout.add(previewLayout)
            verticalLayout.add(propertiesLayout)
            verticalLayout.add(versionsLayout)
            add(verticalLayout)
        }
    }
}