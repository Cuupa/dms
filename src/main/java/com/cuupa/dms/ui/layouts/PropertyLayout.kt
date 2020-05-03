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

    protected lateinit var filename: TextField
    protected lateinit var sender: TextField
    protected lateinit var date: DatePicker
    protected lateinit var time: TimePicker
    protected val tags = MultiSelectListBox<Tag>()

    abstract fun setDocument(document: Document, tags: List<Tag>)

    fun buildGui() {
        filename = TextField(getTranslation(UIConstants.filename))
        sender = TextField(getTranslation(UIConstants.from))
        date = DatePicker(getTranslation(UIConstants.dateOfReciept))
        time = TimePicker(getTranslation(UIConstants.timeOfReciept))

        setSizeFull()
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER)
        justifyContentMode = FlexComponent.JustifyContentMode.START
        val layoutFilename = HorizontalLayout()
        layoutFilename.minWidth = UIConstants.maxSize
        layoutFilename.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layoutFilename.add(filename)
        filename.minWidth = UIConstants.maxSize
        layoutFilename.justifyContentMode = FlexComponent.JustifyContentMode.START
        val layoutSender = getLayout()
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
        val layoutTags = getLayout()
        add(layoutFilename, layoutSender, layoutDateAndTime, layoutTags)
    }

    private fun getLayout(): HorizontalLayout {
        val layout = HorizontalLayout()
        layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER)
        layout.minWidth = UIConstants.maxSize
        sender.minWidth = UIConstants.maxSize
        layout.add(sender)
        layout.justifyContentMode = FlexComponent.JustifyContentMode.START
        return layout
    }
}
