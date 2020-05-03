package com.cuupa.dms.ui.inbox

import com.cuupa.dms.Constants
import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.document.db.DocumentDataProvider
import com.cuupa.dms.ui.MainView
import com.cuupa.dms.ui.dialog.ConfirmDoneDialog
import com.cuupa.dms.ui.dialog.DialogListener
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.selection.SelectionEvent
import com.vaadin.flow.data.selection.SelectionListener
import com.vaadin.flow.router.*
import org.springframework.beans.factory.annotation.Autowired

@Route(value = "inbox", layout = MainView::class)
@RouteAlias(value = "", layout = MainView::class)
class Inbox(@Autowired camundaService: CamundaService, @Autowired val storageService: StorageService, @Autowired val
accessControl: AccessControl) : HorizontalLayout(), HasUrlParameter<String?>, DialogListener {

    private val doneButton = Button("Done")
    private val createTaskButton = Button("Create task")
    private var selectedDocument: Document? = null

    private lateinit var documentPreview: InboxPreview
    private lateinit var horizontalLayout: HorizontalLayout

    companion object {
        const val VIEW_NAME = " Inbox"
    }

    private val itemClickEventComponentEventListener: SelectionListener<Grid<Document?>?, Document?>
        get() = SelectionListener { event: SelectionEvent<Grid<Document?>?, Document?> ->
            if (event.firstSelectedItem.isPresent) {
                selectedDocument = event.firstSelectedItem.get()
                documentPreview.loadImage()
                documentPreview.loadProperties()
                documentPreview.isVisible = true
                doneButton.isEnabled = true
                horizontalLayout.setFlexGrow(1.0, documentPreview)
            } else {
                doneButton.isEnabled = false
                documentPreview.isVisible = false
                horizontalLayout.setFlexGrow(0.0, documentPreview)
            }
        }

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) {
    }

    init {
        setSizeFull()

        createButtons()
        val dataProvider = createMainContent(camundaService, accessControl)
        val searchLayout = createSearchLayout(dataProvider)
        val barAndGridLayout = VerticalLayout(searchLayout)

        barAndGridLayout.add(HorizontalLayout(doneButton, createTaskButton))
        barAndGridLayout.add(horizontalLayout)
        add(barAndGridLayout)
    }

    private fun createMainContent(camundaService: CamundaService, accessControl: AccessControl): DocumentDataProvider {
        val processesForOwner = camundaService.getProcessesForOwner(accessControl.principalName)
        val dataProvider = DocumentDataProvider(storageService, accessControl.principalName, processesForOwner)
        val documentGrid = InboxGrid(storageService)
        documentPreview = InboxPreview(camundaService, storageService, accessControl, documentGrid, storageService
                .findTagsByOwner(accessControl
                        .principalName))
        documentPreview.isVisible = false
        documentGrid.addSelectionListener(itemClickEventComponentEventListener)
        documentGrid.dataProvider = dataProvider

        horizontalLayout = HorizontalLayout()
        horizontalLayout.add(documentGrid, documentPreview)
        horizontalLayout.setFlexGrow(1.0, documentGrid)
        horizontalLayout.setFlexGrow(0.0, documentPreview)
        horizontalLayout.setSizeFull()
        return dataProvider
    }

    private fun createSearchLayout(dataProvider: DocumentDataProvider): HorizontalLayout {
        val filter = TextField()
        filter.width = UIConstants.maxSize
        filter.placeholder = Constants.FILTER_TEXT
        filter.addValueChangeListener { event: AbstractField.ComponentValueChangeEvent<TextField?, String?> -> dataProvider.filter = event.value.toString() }
        filter.isClearButtonVisible = true
        val searchLayout = HorizontalLayout()
        searchLayout.width = UIConstants.maxSize
        val searchButton = Button(getTranslation(UIConstants.search), VaadinIcon.SEARCH.create())
        searchButton.themeName = UIConstants.primaryTheme
        searchButton.addClickListener { dataProvider.filter = filter.value }
        searchButton.width = "10%"
        searchLayout.add(filter)
        searchLayout.add(searchButton)
        return searchLayout
    }

    private fun createButtons() {
        val dialog = ConfirmDoneDialog()
        dialog.setCallBack(this)

        doneButton.themeName = UIConstants.primaryTheme
        doneButton.isEnabled = false
        doneButton.addClickListener { dialog.open() }
        createTaskButton.themeName = UIConstants.primaryTheme
        createTaskButton.isEnabled = false
    }

    override fun consumeEvent(event: ConfirmDoneDialog.Event) {
        if (event == ConfirmDoneDialog.Event.CONFIRM) {
            storageService.complete(selectedDocument)
        }

    }
}