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
        PdfElement view = new PdfElement("object");
        StreamResource
                streamResource =
                new StreamResource(document.getName(), () -> getInputStream(document.getFilename()));
        view.setAttribute("data", streamResource);
        getElement().appendChild(view);
        setSizeFull();
    }

    public PdfView() {

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
