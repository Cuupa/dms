package com.cuupa.dms.ui.fileupload;

import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.ui.documentviews.PdfView;
import com.cuupa.dms.ui.overview.DocumentsOverview;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Vector;

public class PreviewAndPropertiesLayout extends VerticalLayout {

    private final Button previousButton = new Button("Previeous");

    private final Button nextButton = new Button("Next");

    private final Button save = new Button("Save");

    private final Button cancel = new Button("Cancel");

    private final HorizontalLayout contentLayout = new HorizontalLayout();

    private final List<FileUploadProperties> properties = new Vector<>();

    private final List<PdfView> preview = new Vector<>();

    private FileUploadProperties lastPropertiesLayout = new FileUploadProperties();

    private VerticalLayout lastPreviewLayout = new VerticalLayout();

    private int index = 0;

    public PreviewAndPropertiesLayout(@Autowired StorageService storageService) {
        initButtons(storageService);
        lastPreviewLayout.setVisible(false);
        lastPropertiesLayout.setVisible(false);
        setMinWidth("90%");
        setHeight("90%");
        contentLayout.add(lastPreviewLayout, lastPropertiesLayout);
        contentLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        contentLayout.setWidth("100%");
        HorizontalLayout horizontalLayout = new HorizontalLayout(previousButton, contentLayout, nextButton);
        horizontalLayout.setSizeFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        add(horizontalLayout);
        setJustifyContentMode(JustifyContentMode.START);

        setContent();

        final HorizontalLayout horizontalLayoutSaveCancel = new HorizontalLayout();
        horizontalLayoutSaveCancel.add(cancel, save);
        horizontalLayoutSaveCancel.setMinWidth("100%");
        horizontalLayoutSaveCancel.setJustifyContentMode(JustifyContentMode.END);
        add(horizontalLayoutSaveCancel);

        setSizeFull();
    }

    public void allFinishedEvent() {
        previousButton.setVisible(true);
        nextButton.setVisible(true);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        setContent();
    }

    private void incrementIndex() {
        if (index < properties.size() - 1) {
            index++;
            setContent();
        } else {
            nextButton.setEnabled(false);
        }
    }

    private void decrementIndex() {
        if (index > 0) {
            index--;
            setContent();
        } else {
            previousButton.setEnabled(false);
        }
    }

    private void setContent() {
        if (!properties.isEmpty()) {
            contentLayout.replace(lastPropertiesLayout, properties.get(index));
            lastPropertiesLayout = properties.get(index);
            lastPropertiesLayout.setHeight("100%");
            lastPropertiesLayout.setWidth("50%");
            save.setEnabled(true);
        }


        if (!preview.isEmpty()) {
            contentLayout.replace(lastPreviewLayout, preview.get(index));
            lastPreviewLayout = preview.get(index);
            lastPreviewLayout.setHeight("100%");
            lastPreviewLayout.setWidth("50%");
        }

        if (index > 0) {
            previousButton.setEnabled(true);
        } else if (index < properties.size() - 1) {
            nextButton.setEnabled(true);
        }

    }

    private void initButtons(StorageService storageService) {
        previousButton.setMinWidth("10%");
        nextButton.setMinWidth("10%");

        previousButton.setVisible(false);
        nextButton.setVisible(false);
        previousButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
        nextButton.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        previousButton.addClickListener(event -> decrementIndex());

        nextButton.addClickListener(event -> incrementIndex());

        save.setEnabled(false);
        save.setThemeName("primary");
        save.addClickListener(event -> {

            for (FileUploadProperties property : properties) {
                new DocumentSaveUtil(property, storageService).save();
            }
            save.getUI().ifPresent(ui -> ui.navigate(DocumentsOverview.VIEW_NAME));
        });

        cancel.addClickListener(event -> cancel.getUI().ifPresent(ui -> ui.navigate(DocumentsOverview.VIEW_NAME)));
    }

    public List<FileUploadProperties> getProperties() {
        return properties;
    }

    public List<PdfView> getPreview() {
        return preview;
    }
}
