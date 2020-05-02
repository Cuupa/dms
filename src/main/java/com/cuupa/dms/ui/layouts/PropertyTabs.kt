package com.cuupa.dms.ui.layouts

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.Stream

class PropertyTabs(previewLayout: VerticalLayout,
                   propertyLayout: PropertyLayout,
                   versionsLayout: VerticalLayout) : VerticalLayout() {

    init {
        val tabPreview = Tab(getTranslation("preview"))
        val tabProperties = Tab(getTranslation("properties"))
        val tabVersions = Tab(getTranslation("versions"))
        val tabs = Tabs(tabPreview, tabProperties, tabVersions)
        val tabsToPages: MutableMap<Tab, Component> = HashMap()
        tabsToPages[tabPreview] = previewLayout
        tabsToPages[tabProperties] = propertyLayout
        tabsToPages[tabVersions] = versionsLayout
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
