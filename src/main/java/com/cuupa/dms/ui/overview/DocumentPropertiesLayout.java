package com.cuupa.dms.ui.overview;

import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.util.List;

public class DocumentPropertiesLayout extends VerticalLayout {

    private final TextField filename = new TextField("Filename");

    private final TextField sender = new TextField("From");

    private final DatePicker date = new DatePicker("Date");

    private final TimePicker time = new TimePicker("Time");

    private final MultiSelectListBox<Tag> tags = new MultiSelectListBox<>();

    private Document document;

    public DocumentPropertiesLayout() {
        setSizeFull();
        setMinWidth("100%");
        setHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        final HorizontalLayout layoutFilename = new HorizontalLayout();
        layoutFilename.setMinWidth("100%");
        layoutFilename.setVerticalComponentAlignment(Alignment.CENTER);
        layoutFilename.add(filename);
        filename.setMinWidth("100%");
        layoutFilename.setJustifyContentMode(JustifyContentMode.START);

        final HorizontalLayout layoutSender = new HorizontalLayout();
        layoutSender.setVerticalComponentAlignment(Alignment.CENTER);
        layoutSender.setMinWidth("100%");
        sender.setMinWidth("100%");
        layoutSender.add(sender);
        layoutSender.setJustifyContentMode(JustifyContentMode.START);

        final HorizontalLayout layoutDateAndTime = new HorizontalLayout();
        date.setMinWidth("50%");
        time.setMinWidth("50%");
        layoutDateAndTime.add(date, time);
        layoutDateAndTime.setFlexGrow(1, date, time);

        final HorizontalLayout layoutDate = new HorizontalLayout();
        layoutDate.setMinWidth("100%");
        layoutDate.setVerticalComponentAlignment(Alignment.CENTER);
        layoutDate.add(layoutDateAndTime);
        layoutDate.setJustifyContentMode(JustifyContentMode.START);

        final HorizontalLayout layoutTags = new HorizontalLayout();
        layoutTags.setVerticalComponentAlignment(Alignment.CENTER);
        layoutTags.setMinWidth("100%");
        tags.setMinWidth("100%");
        layoutTags.add(tags);
        layoutTags.setJustifyContentMode(JustifyContentMode.START);

        add(layoutFilename);
        add(layoutSender);
        add(layoutDate);
        add(layoutTags);
    }

    public void setDocument(final Document document, List<Tag> tags) {
        this.document = document;
        this.filename.setValue(document.getName());
        this.sender.setValue(document.getSender());
        this.date.setValue(document.getCreateDate().toLocalDate());
        this.time.setValue(document.getCreateDate().toLocalTime());
        this.tags.setItems(tags);
        //this.tags.updateSelection(new HashSet<>(document.getTags()), new HashSet<>());
        //this.tags.setValue();
    }
}
