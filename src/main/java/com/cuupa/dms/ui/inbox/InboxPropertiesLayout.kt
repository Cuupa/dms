package com.cuupa.dms.ui.inbox

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.cuupa.dms.ui.layouts.PropertyLayout
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class InboxPropertiesLayout(@Autowired val camundaService: CamundaService, @Autowired val accessControl: AccessControl) : PropertyLayout() {

    private lateinit var document: Document

    private val dueDate = DatePicker(getTranslation(UIConstants.duedate))

    override fun setDocument(document: Document, tags: List<Tag>) {
        this.document = document
        filename.value = document.name
        sender.value = document.sender
        date.value = document.createDate.toLocalDate()
        time.value = document.createDate.toLocalTime()
        this.tags.setItems(tags)
        val dueDateString = camundaService.getProcessByProcessInstanceId(document.processInstanceId, "dueDate")
                .let { list ->
                    list?.map { variableInstance -> variableInstance.value }?.firstOrNull()
                }

        if (dueDateString != null && dueDateString.toString().isBlank()) {
            dueDate.value = LocalDate.parse(dueDateString.toString())
        }

        //dueDate.value = processByProcessInstanceId.let { processInstance -> processInstance. }
        //this.tags.updateSelection(new HashSet<>(document.getTags()), new HashSet<>());
//this.tags.setValue();
    }

    init {
        if (accessControl.isUserSingedIn) {
            buildGui()
            val layoutDueDate = HorizontalLayout()
            layoutDueDate.minWidth = UIConstants.maxSize
            layoutDueDate.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
            layoutDueDate.add(dueDate)
            add(layoutDueDate)
        }
    }
}