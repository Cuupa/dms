package com.cuupa.dms.ui.fileupload;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.server.StreamResource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class FileUploadProperties extends VerticalLayout {

    private final TextField filename = new TextField("Filename");

    private final TextField from = new TextField("From");

    private final DatePicker date = new DatePicker("Date");

    private final TimePicker time = new TimePicker("Time");

    private final TextField tags = new TextField("Tags");

    private byte[] content = new byte[0];

    private EmbeddedPdfDocument pdf = new EmbeddedPdfDocument();

    public FileUploadProperties() {
        filename.setMinWidth("100%");
        from.setMinWidth("100%");
        date.setMinWidth("50%");
        time.setMinWidth("50%");
        tags.setMinWidth("100%");
        pdf.setMinWidth("100%");
        add(filename);
        add(from);
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(date);
        horizontalLayout.add(time);
        add(horizontalLayout);
        add(tags);
        add(pdf);
        setSizeFull();
    }

    public String getFilename() {
        return this.filename.getValue();
    }

    public void setFilename(String filename) {
        this.filename.setValue(filename);
    }

    public String getFrom() {
        return this.from.getValue();
    }

    public void setFrom(String from) {
        this.from.setValue(from);
    }

    public LocalDate getDate() {
        return this.date.getValue();
    }

    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }

    public LocalTime getTime() {
        return this.time.getValue();
    }

    public void setTime(LocalTime time) {
        this.time.setValue(time);
    }

    public List<String> getTags() {
        return Arrays.asList(this.tags.getValue().split(","));
    }

    public void setTags(List<String> tags) {
        this.tags.setValue(String.join(",", tags));
    }

    public void setPdf(StreamResource resource) {
        pdf = new EmbeddedPdfDocument(resource);
        pdf.setHeight("50%");
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
