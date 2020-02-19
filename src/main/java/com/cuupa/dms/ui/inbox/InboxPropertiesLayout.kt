package com.cuupa.dms.ui.inbox

import com.cuupa.dms.UIConstants
import com.cuupa.dms.service.CamundaService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.listbox.MultiSelectListBox
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.timepicker.TimePicker
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class InboxPropertiesLayout(@Autowired val camundaService: CamundaService) : VerticalLayout() {

    private lateinit var document: Document
    private val filename = TextField(UIConstants.filename)
    private val sender = TextField(UIConstants.from)
    private val date = DatePicker(UIConstants.date)
    private val time = TimePicker(UIConstants.time)
    private val dueDate = DatePicker("duedate")
    private val tags = MultiSelectListBox<Tag>()


    fun setDocument(document: Document, tags: List<Tag>) {
        this.document = document
        filename.value = document.name
        sender.value = document.sender
        date.value = document.createDate.toLocalDate()
        time.value = document.createDate.toLocalTime()
        this.tags.setItems(tags)
        val dueDateString = camundaService.getProcessByProcessInstanceId(document.processInstanceId, "dueDate")
                .let { list ->
                    list?.map { variableInstance -> variableInstance.value }?.first()
                }

        if (dueDateString != null && dueDateString.toString().isNullOrBlank()) {
            dueDate.value = LocalDate.parse(dueDateString.toString())
        }

        //dueDate.value = processByProcessInstanceId.let { processInstance -> processInstance. }
        //this.tags.updateSelection(new HashSet<>(document.getTags()), new HashSet<>());
//this.tags.setValue();
    }

    init {
        setSizeFull()
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER)
        justifyContentMode = JustifyContentMode.START
        val layoutFilename = HorizontalLayout()
        layoutFilename.minWidth = UIConstants.maxSize
        layoutFilename.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutFilename.add(filename)
        filename.minWidth = UIConstants.maxSize
        layoutFilename.justifyContentMode = JustifyContentMode.START
        val layoutSender = HorizontalLayout()
        layoutSender.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutSender.minWidth = UIConstants.maxSize
        sender.minWidth = UIConstants.maxSize
        layoutSender.add(sender)
        layoutSender.justifyContentMode = JustifyContentMode.START
        val layoutDateAndTime = HorizontalLayout()
        date.minWidth = UIConstants.halfSize
        time.minWidth = UIConstants.halfSize
        layoutDateAndTime.add(date, time)
        layoutDateAndTime.setFlexGrow(1.0, date, time)
        val layoutDate = HorizontalLayout()
        layoutDate.minWidth = UIConstants.maxSize
        layoutDate.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutDate.add(layoutDateAndTime)
        layoutDate.justifyContentMode = JustifyContentMode.START
        val layoutTags = HorizontalLayout()
        layoutTags.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutTags.minWidth = UIConstants.maxSize
        tags.minWidth = UIConstants.maxSize
        layoutTags.add(tags)
        layoutTags.justifyContentMode = JustifyContentMode.START
        val layoutDueDate = HorizontalLayout()
        layoutDueDate.minWidth = UIConstants.maxSize
        layoutDueDate.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutDueDate.add(dueDate)
        add(layoutFilename, layoutSender, layoutDate, layoutTags)
    }
}