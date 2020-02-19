package com.cuupa.dms.ui.inbox

import com.cuupa.dms.UIConstants
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.ui.documentviews.PdfView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.Stream

class InboxPreview(@Autowired val camundaService: CamundaService, private val documentGrid: Grid<Document>, private val tagsByOwner: List<Tag>) : HorizontalLayout() {

    private val previewLayout = VerticalLayout()
    private var lastView = PdfView()
    private val propertiesLayout = InboxPropertiesLayout(camundaService)
    fun loadImage() {
        val document = documentGrid.asSingleSelect().value
        if (document != null) {
            val pdfView = PdfView(document)
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

    private fun createTabs(): Tabs {
        val tabPreview = Tab()
        tabPreview.label = UIConstants.preview
        val tabProperties = Tab()
        tabProperties.label = UIConstants.properties
        val tabs = Tabs(tabPreview, tabProperties)
        val tabsToPages: MutableMap<Tab, Component> = HashMap()
        tabsToPages[tabPreview] = previewLayout
        tabsToPages[tabProperties] = propertiesLayout
        val pagesShown: MutableSet<Component?> = Stream.of(previewLayout).collect(Collectors.toSet())
        tabs.addSelectedChangeListener {
            pagesShown.forEach(Consumer { page: Component? -> page!!.isVisible = false })
            pagesShown.clear()
            val selectedPage = tabsToPages[tabs.selectedTab]
            selectedPage!!.isVisible = true
            pagesShown.add(selectedPage)
        }
        return tabs
    }

    init {
        setSizeFull()
        val verticalLayout = VerticalLayout()
        previewLayout.add(lastView)
        previewLayout.setSizeFull()
        propertiesLayout.isVisible = false
        verticalLayout.add(createTabs())
        verticalLayout.add(previewLayout)
        verticalLayout.add(propertiesLayout)
        add(verticalLayout)
    }
}