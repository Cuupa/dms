package com.cuupa.dms.ui.inbox

import com.cuupa.dms.Constants
import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.document.db.DocumentDataProvider
import com.cuupa.dms.ui.MainView
import com.cuupa.dms.ui.overview.DocumentGrid
import com.cuupa.dms.ui.overview.DocumentPreview
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.selection.SelectionEvent
import com.vaadin.flow.data.selection.SelectionListener
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.OptionalParameter
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired

@Route(value = "inbox", layout = MainView::class)
class Inbox(@Autowired camundaService: CamundaService, @Autowired storageService: StorageService, @Autowired accessControl: AccessControl) : HorizontalLayout(), HasUrlParameter<String?> {

    private val documentPreview: DocumentPreview
    private val horizontalLayout: HorizontalLayout
    private val dataProvider: DocumentDataProvider
    private val itemClickEventComponentEventListener: SelectionListener<Grid<Document?>?, Document?>
        get() = SelectionListener { event: SelectionEvent<Grid<Document?>?, Document?> ->
            if (event.firstSelectedItem.isPresent) {
                documentPreview.loadImage()
                documentPreview.loadProperties()
                documentPreview.isVisible = true
                horizontalLayout.setFlexGrow(1.0, documentPreview)
            } else {
                documentPreview.isVisible = false
                horizontalLayout.setFlexGrow(0.0, documentPreview)
            }
        }

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) {
    }

    companion object {
        const val VIEW_NAME = " Inbox"
    }

    init {
        setSizeFull()
        val processesForOwner = camundaService.getProcessesForOwner(accessControl.principalName)
        dataProvider = DocumentDataProvider(storageService, accessControl.principalName, processesForOwner)
        val documentGrid = DocumentGrid()
        documentPreview = DocumentPreview(documentGrid, storageService.findTagsByOwner(accessControl.principalName))
        documentPreview.isVisible = false
        documentGrid.addSelectionListener(itemClickEventComponentEventListener)
        documentGrid.dataProvider = dataProvider
        val filter = TextField()
        filter.width = UIConstants.maxSize
        filter.placeholder = Constants.FILTER_TEXT
        filter.addValueChangeListener { event: AbstractField.ComponentValueChangeEvent<TextField?, String?> -> dataProvider.filter = event.value.toString() }
        horizontalLayout = HorizontalLayout()
        horizontalLayout.add(documentGrid, documentPreview)
        horizontalLayout.setFlexGrow(1.0, documentGrid)
        horizontalLayout.setFlexGrow(0.0, documentPreview)
        horizontalLayout.setSizeFull()
        val barAndGridLayout = VerticalLayout()
        val searchLayout = HorizontalLayout()
        searchLayout.width = UIConstants.maxSize
        val searchButton = Button(UIConstants.search, VaadinIcon.SEARCH.create())
        searchButton.themeName = UIConstants.primaryTheme
        searchButton.addClickListener { dataProvider.filter = filter.value }
        searchButton.width = "10%"
        searchLayout.add(filter)
        searchLayout.add(searchButton)
        filter.isClearButtonVisible = true
        barAndGridLayout.add(searchLayout)
        barAndGridLayout.add(horizontalLayout)
        add(barAndGridLayout)
    }
}