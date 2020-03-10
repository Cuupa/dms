package com.cuupa.dms.ui.layouts

import com.cuupa.dms.UIConstants
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.listbox.MultiSelectListBox
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.timepicker.TimePicker

abstract class PropertyLayout : VerticalLayout() {

    protected val filename = TextField(UIConstants.filename)
    protected val sender = TextField(UIConstants.from)
    protected val date = DatePicker(UIConstants.date)
    protected val time = TimePicker(UIConstants.time)
    protected val tags = MultiSelectListBox<Tag>()

    abstract fun setDocument(document: Document, tags: List<Tag>)

    fun buildGui() {
        setSizeFull()
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER)
        justifyContentMode = FlexComponent.JustifyContentMode.START
        val layoutFilename = HorizontalLayout()
        layoutFilename.minWidth = UIConstants.maxSize
        layoutFilename.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutFilename.add(filename)
        filename.minWidth = UIConstants.maxSize
        layoutFilename.justifyContentMode = FlexComponent.JustifyContentMode.START
        val layoutSender = HorizontalLayout()
        layoutSender.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutSender.minWidth = UIConstants.maxSize
        sender.minWidth = UIConstants.maxSize
        layoutSender.add(sender)
        layoutSender.justifyContentMode = FlexComponent.JustifyContentMode.START
        val layoutDateAndTime = HorizontalLayout()
        date.minWidth = UIConstants.halfSize
        time.minWidth = UIConstants.halfSize
        layoutDateAndTime.add(date, time)
        layoutDateAndTime.setFlexGrow(1.0, date, time)
        val layoutDate = HorizontalLayout()
        layoutDate.minWidth = UIConstants.maxSize
        layoutDate.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutDate.add(layoutDateAndTime)
        layoutDate.justifyContentMode = FlexComponent.JustifyContentMode.START
        val layoutTags = HorizontalLayout()
        layoutTags.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutTags.minWidth = UIConstants.maxSize
        tags.minWidth = UIConstants.maxSize
        layoutTags.add(tags)
        layoutTags.justifyContentMode = FlexComponent.JustifyContentMode.START
        add(layoutFilename, layoutSender, layoutDate, layoutTags)
    }
}
