package com.cuupa.dms.ui.fileupload;

import com.cuupa.dms.Constants;
import com.cuupa.dms.authentication.AccessControlFactory;
import com.cuupa.dms.service.extern.ExternSemanticService;
import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;
import com.cuupa.dms.ui.MainView;
import com.cuupa.dms.ui.overview.DocumentsOverview;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Route(value = "Upload", layout = MainView.class)
public class FileUploadOverview extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Upload";

    private final VerticalLayout layout = new VerticalLayout();

    private final StorageService storageService;

    private final HorizontalLayout horizontalLayout = new HorizontalLayout();

    private final List<FileUploadProperties> properties = new Vector<>();

    private final Button previous = new Button("Previeous");

    private final Button next = new Button("Next");

    private final Button save = new Button("Save");

    private final Button cancel = new Button("Cancel");

    private FileUploadProperties lastLayout = new FileUploadProperties();

    private int index = 0;

    public FileUploadOverview(@Autowired ExternSemanticService externSemanticService, @Autowired StorageService storageService) {
        this.storageService = storageService;

        final Upload upload = createFileUpload(externSemanticService);

        setContent();
        lastLayout.setVisible(false);
        layout.add(upload);

        initButtons();
        horizontalLayout.add(previous);
        horizontalLayout.add(lastLayout);
        horizontalLayout.add(next);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        previous.setMinWidth("10%");
        next.setMinWidth("10%");
        horizontalLayout.setMinWidth("90%");
        layout.add(horizontalLayout);

        final HorizontalLayout horizontalLayoutSaveCancel = new HorizontalLayout();
        horizontalLayoutSaveCancel.add(cancel);
        horizontalLayoutSaveCancel.add(save);
        horizontalLayoutSaveCancel.setMinWidth("100%");
        horizontalLayoutSaveCancel.setJustifyContentMode(JustifyContentMode.END);
        layout.add(horizontalLayoutSaveCancel);
        layout.setMinWidth("100%");
        setMaxWidth("100%");

        //tags.setItems(storageService.findTagsByOwner(AccessControlFactory.getInstance().createAccessControl().getPrincipalName()).stream().map(Tag::getName).collect(
        //       Collectors.toList()));
        add(layout);
    }

    private Upload createFileUpload(@Autowired ExternSemanticService externSemanticService) {
        final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        final Upload upload = new Upload(buffer);
        upload.addSucceededListener(new FileUploadSucceededListener(externSemanticService, properties, buffer));
        upload.addAllFinishedListener(event -> {
            previous.setVisible(true);
            next.setVisible(true);
            previous.setEnabled(false);
            next.setEnabled(false);
            setContent();
        });

        upload.setMinWidth("100%");
        return upload;
    }

    private void initButtons() {
        previous.setVisible(false);
        next.setVisible(false);
        previous.setIcon(VaadinIcon.ANGLE_LEFT.create());
        next.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        previous.addClickListener(event -> {
            decrementIndex();
        });

        next.addClickListener(event -> {
            incrementIndex();
        });

        save.setEnabled(false);
        save.addClickListener(event -> {

            for (FileUploadProperties property : properties) {
                new DocumentSaveUtil(property, storageService).save();
            }
            save.getUI().ifPresent(ui -> ui.navigate(DocumentsOverview.VIEW_NAME));
        });

        cancel.addClickListener(event -> {
            cancel.getUI().ifPresent(ui -> ui.navigate(DocumentsOverview.VIEW_NAME));
        });
    }

    private void incrementIndex() {
        if (index < properties.size() - 1) {
            index++;
            setContent();
        } else {
            next.setEnabled(false);
        }
    }

    private void decrementIndex() {
        if (index > 0) {
            index--;
            setContent();
        } else {
            previous.setEnabled(false);
        }
    }

    private void setContent() {
        if (!properties.isEmpty()) {
            horizontalLayout.replace(lastLayout, properties.get(index));
            lastLayout = properties.get(index);
            save.setEnabled(true);
        }

        if (index > 0) {
            previous.setEnabled(true);
        } else if (index < properties.size() - 1) {
            next.setEnabled(true);
        }

    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        //viewLogic.enter(parameter);
    }

    private static class DocumentSaveUtil {

        private final FileUploadProperties properties;

        private final StorageService storageService;

        protected DocumentSaveUtil(FileUploadProperties properties, StorageService storageService) {
            this.properties = properties;
            this.storageService = storageService;
        }

        public void save() {
            if (writeFile()) {
                writeDatabase();
            }
        }

        private void writeDatabase() {
            storageService.save(new Document(0L,
                                             properties.getFilename(),
                                             properties.getFrom(),
                                             AccessControlFactory.getInstance()
                                                                 .createAccessControl()
                                                                 .getPrincipalName(),
                                             LocalDateTime.of(properties.getDate(), properties.getTime()),
                                             properties.getTags().stream().map(Tag::new).collect(Collectors.toList())));
        }

        private boolean writeFile() {
            try {
                Path
                        path =
                        Paths.get(Constants.DOCUMENTFOLDER +
                                  File.separator +
                                  AccessControlFactory.getInstance().createAccessControl().getPrincipalName() +
                                  File.separator +
                                  properties.getFilename());
                path.toFile().mkdirs();
                if (!Files.exists(path)) {
                    Files.write(path, properties.getContent(), StandardOpenOption.CREATE_NEW);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}

