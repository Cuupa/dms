package com.cuupa.dms.ui.fileupload

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.ui.documentviews.PdfView
import com.cuupa.dms.ui.overview.DocumentsOverview
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class PreviewAndPropertiesLayout(@Autowired storageService: StorageService, @Autowired camundaService: CamundaService, @Autowired accessControl: AccessControl) : VerticalLayout() {

    private val previousButton = Button(UIConstants.previous)
    private val nextButton = Button(UIConstants.next)
    private val save = Button(UIConstants.save)
    private val cancel = Button(UIConstants.cancel)
    private val contentLayout = HorizontalLayout()

    val properties: MutableList<FileUploadProperties> = Vector()
    val preview: MutableList<PdfView> = Vector()

    private var lastPropertiesLayout = FileUploadProperties()
    private var lastPreviewLayout = VerticalLayout()
    private var index = 0

    fun allFinishedEvent() {
        previousButton.isVisible = true
        nextButton.isVisible = true
        previousButton.isEnabled = false
        nextButton.isEnabled = false
        setContent()
    }

    private fun incrementIndex() {
        if (index < properties.size - 1) {
            index.inc()
            setContent()
        } else {
            nextButton.isEnabled = false
        }
    }

    private fun decrementIndex() {
        if (index > 0) {
            index.dec()
            setContent()
        } else {
            previousButton.isEnabled = false
        }
    }

    private fun setContent() {
        if (properties.isNotEmpty()) {
            contentLayout.replace(lastPropertiesLayout, properties[index])
            lastPropertiesLayout = properties[index]
            lastPropertiesLayout.height = UIConstants.maxSize
            lastPropertiesLayout.width = UIConstants.halfSize
            save.isEnabled = true
        }
        if (!preview.isEmpty()) {
            contentLayout.replace(lastPreviewLayout, preview[index])
            lastPreviewLayout = preview[index]
            lastPreviewLayout.height = UIConstants.maxSize
            lastPreviewLayout.width = UIConstants.halfSize
        }
        if (index > 0) {
            previousButton.isEnabled = true
        } else if (index < properties.size - 1) {
            nextButton.isEnabled = true
        }
    }

    private fun initButtons(storageService: StorageService, camundaService: CamundaService, accessControl: AccessControl) {
        previousButton.minWidth = "10%"
        nextButton.minWidth = "10%"
        previousButton.isVisible = false
        nextButton.isVisible = false
        previousButton.icon = VaadinIcon.ANGLE_LEFT.create()
        nextButton.icon = VaadinIcon.ANGLE_RIGHT.create()
        previousButton.addClickListener { decrementIndex() }
        nextButton.addClickListener { incrementIndex() }
        save.isEnabled = false
        save.themeName = UIConstants.primaryTheme
        save.addClickListener {
            properties.forEach { property ->
                DocumentSaveUtil(property, storageService, camundaService, accessControl.principalName).save()
            }
            save.ui.ifPresent { ui: UI -> ui.navigate(DocumentsOverview.VIEW_NAME) }
        }
        cancel.addClickListener { cancel.ui.ifPresent { ui: UI -> ui.navigate(DocumentsOverview.VIEW_NAME) } }
    }

    init {
        initButtons(storageService, camundaService, accessControl)
        lastPreviewLayout.isVisible = false
        lastPropertiesLayout.isVisible = false
        minWidth = "90%"
        height = "90%"
        contentLayout.add(lastPreviewLayout, lastPropertiesLayout)
        contentLayout.justifyContentMode = JustifyContentMode.CENTER
        contentLayout.width = UIConstants.maxSize
        val horizontalLayout = HorizontalLayout(previousButton, contentLayout, nextButton)
        horizontalLayout.setSizeFull()
        horizontalLayout.justifyContentMode = JustifyContentMode.CENTER
        add(horizontalLayout)
        justifyContentMode = JustifyContentMode.START
        setContent()
        val horizontalLayoutSaveCancel = HorizontalLayout()
        horizontalLayoutSaveCancel.add(cancel, save)
        horizontalLayoutSaveCancel.minWidth = UIConstants.maxSize
        horizontalLayoutSaveCancel.justifyContentMode = JustifyContentMode.END
        add(horizontalLayoutSaveCancel)
        setSizeFull()
    }
}