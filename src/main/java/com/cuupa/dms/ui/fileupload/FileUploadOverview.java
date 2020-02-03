package com.cuupa.dms.ui.fileupload;

import com.cuupa.dms.service.extern.ExternSemanticService;
import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.ui.MainView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "Upload", layout = MainView.class)
public class FileUploadOverview extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Upload";

    private final VerticalLayout layout = new VerticalLayout();

    public FileUploadOverview(@Autowired ExternSemanticService externSemanticService, @Autowired StorageService storageService) {
        final PreviewAndPropertiesLayout previewAndPropertiesLayout = new PreviewAndPropertiesLayout(storageService);

        final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        final Upload upload = new Upload(buffer);
        upload.addSucceededListener(new FileUploadSucceededListener(externSemanticService,
                                                                    buffer,
                                                                    previewAndPropertiesLayout.getProperties(),
                                                                    previewAndPropertiesLayout.getPreview()));
        upload.addAllFinishedListener(event -> previewAndPropertiesLayout.allFinishedEvent());

        upload.setMinWidth("100%");
        layout.add(upload);
        layout.add(previewAndPropertiesLayout);
        setSizeFull();
        //tags.setItems(storageService.findTagsByOwner(AccessControlFactory.getInstance().createAccessControl().getPrincipalName()).stream().map(Tag::getName).collect(
        //       Collectors.toList()));
        add(layout);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        //viewLogic.enter(parameter);
    }
}

