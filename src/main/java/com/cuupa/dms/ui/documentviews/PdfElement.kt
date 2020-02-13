package com.cuupa.dms.ui.documentviews;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.dom.Element;

public class PdfElement extends Element implements HasSize {

    private static final String mediaType = "application/pdf";

    public PdfElement(String tag) {
        super(tag);
        setAttribute("type", mediaType);
        getStyle().set("display", "block");
        setSizeFull();
    }

    @Override
    public Element getElement() {
        return this;
    }
}
