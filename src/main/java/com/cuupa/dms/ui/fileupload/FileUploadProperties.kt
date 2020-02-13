package com.cuupa.dms.ui.fileupload

import com.cuupa.dms.UIConstants
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.timepicker.TimePicker
import java.time.LocalDate
import java.time.LocalTime

class FileUploadProperties : VerticalLayout() {

    private val filename = TextField("Filename")
    private val from = TextField("From")
    private val date = DatePicker("Date")
    private val time = TimePicker("Time")
    private val tags = TextField("Tags")

    var content = ByteArray(0)

    fun getFilename(): String {
        return filename.value
    }

    fun setFilename(filename: String) {
        this.filename.value = filename
    }

    fun getFrom(): String {
        return from.value
    }

    fun setFrom(from: String?) {
        this.from.value = from
    }

    fun getDate(): LocalDate {
        return date.value
    }

    fun setDate(date: LocalDate) {
        this.date.value = date
    }

    fun getTime(): LocalTime {
        return time.value
    }

    fun setTime(time: LocalTime) {
        this.time.value = time
    }

    fun getTags(): List<String> {
        return listOf(*tags.value.split(",").toTypedArray())
    }

    fun setTags(tags: List<String>) {
        this.tags.value = java.lang.String.join(",", tags)
    }

    init {
        filename.minWidth = UIConstants.maxSize
        from.minWidth = UIConstants.maxSize
        date.minWidth = UIConstants.halfSize
        time.minWidth = UIConstants.halfSize
        tags.minWidth = UIConstants.maxSize
        add(filename, from)

        val horizontalLayout = HorizontalLayout(date, time)
        horizontalLayout.width = UIConstants.maxSize
        add(horizontalLayout, tags)
        setSizeFull()
    }
}