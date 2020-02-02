package com.cuupa.dms.ui.overview;

import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;
import com.cuupa.dms.ui.documentviews.PdfView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentPreview extends HorizontalLayout {

    private final DocumentGrid documentGrid;

    private final VerticalLayout previewLayout = new VerticalLayout();

    private PdfView lastView = new PdfView();

    private final DocumentPropertiesLayout propertiesLayout = new DocumentPropertiesLayout();

    private List<Tag> tagsByOwner;

    public DocumentPreview(DocumentGrid documentGrid, List<Tag> tagsByOwner) {
        this.documentGrid = documentGrid;
        this.tagsByOwner = tagsByOwner;
        setSizeFull();

        final Tabs tabs = createTabs();
        final VerticalLayout verticalLayout = new VerticalLayout();
        previewLayout.add(lastView);
        verticalLayout.add(tabs);
        previewLayout.setSizeFull();
        verticalLayout.add(previewLayout);
        verticalLayout.add(propertiesLayout);
        propertiesLayout.setVisible(false);
        add(verticalLayout);
    }

    public void loadImage() {
        final Document document = documentGrid.asSingleSelect().getValue();
        if (document != null) {
            PdfView pdfView = new PdfView(document);
            previewLayout.replace(lastView, pdfView);
            lastView = pdfView;
        }
    }

    public void loadProperties() {
        final Document document = documentGrid.asSingleSelect().getValue();
        if (document != null) {
            propertiesLayout.setDocument(document, tagsByOwner);
        }
    }

    private Tabs createTabs() {
        final Tab tabPreview = new Tab();
        tabPreview.setLabel("Preview");

        final Tab tabProperties = new Tab();
        tabProperties.setLabel("Properties");

        final Tabs tabs = new Tabs(tabPreview, tabProperties);

        final Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tabPreview, previewLayout);
        tabsToPages.put(tabProperties, propertiesLayout);

        final Set<Component> pagesShown = Stream.of(previewLayout).collect(Collectors.toSet());

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });
        return tabs;
    }
}
