package com.cuupa.dms.ui.documentviews;

import com.cuupa.dms.storage.document.Document;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfView extends VerticalLayout {

    public PdfView(final Document document) {
        StreamResource
                streamResource =
                new StreamResource(document.getName(), () -> getInputStream(document.getFilename()));
        init(streamResource);
    }

    public PdfView() {

    }

    public PdfView(final StreamResource streamResource) {
        init(streamResource);
    }

    private void init(StreamResource streamResource) {
        PdfElement view = new PdfElement("object");
        view.setAttribute("data", streamResource);
        getElement().appendChild(view);
        setSizeFull();
    }

    private InputStream getInputStream(final String filename) {
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            return new ByteArrayInputStream(inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
