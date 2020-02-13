package com.cuupa.dms.ui.overview

import com.cuupa.dms.UIConstants
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

class DocumentPropertiesLayout : VerticalLayout() {

    private val filename = TextField(UIConstants.filename)
    private val sender = TextField(UIConstants.from)
    private val date = DatePicker(UIConstants.date)
    private val time = TimePicker(UIConstants.time)
    private val tags = MultiSelectListBox<Tag>()

    fun setDocument(document: Document, tags: List<Tag>) {
        filename.value = document.name
        sender.value = document.sender
        date.value = document.createDate.toLocalDate()
        time.value = document.createDate.toLocalTime()
        this.tags.setItems(tags)
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
        add(layoutFilename, layoutSender, layoutDate, layoutTags)
    }
}