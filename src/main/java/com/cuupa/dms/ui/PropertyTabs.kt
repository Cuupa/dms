package com.cuupa.dms.ui

import com.cuupa.dms.UIConstants
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.Stream

class PropertyTabs(previewLayout: VerticalLayout,
                   propertyLayout: PropertyLayout) : VerticalLayout() {

    init {
        val tabPreview = Tab()
        tabPreview.label = UIConstants.preview
        val tabProperties = Tab()
        tabProperties.label = UIConstants.properties
        val tabs = Tabs(tabPreview, tabProperties)
        val tabsToPages: MutableMap<Tab, Component> = HashMap()
        tabsToPages[tabPreview] = previewLayout
        tabsToPages[tabProperties] = propertyLayout
        val pagesShown: MutableSet<Component?> = Stream.of(previewLayout).collect(Collectors.toSet())
        tabs.addSelectedChangeListener {
            pagesShown.forEach(Consumer { page: Component? -> page!!.isVisible = false })
            pagesShown.clear()
            val selectedPage = tabsToPages[tabs.selectedTab]
            selectedPage!!.isVisible = true
            pagesShown.add(selectedPage)
        }
        add(tabs)
    }
}
