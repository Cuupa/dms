package com.cuupa.dms.ui.overview;

import com.cuupa.dms.Constants;
import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.document.db.DocumentDataProvider;
import com.cuupa.dms.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "documents", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
public class DocumentsOverview extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = " Documents";

    private final DocumentPreview documentPreview;

    private final HorizontalLayout horizontalLayout;

    private final DocumentDataProvider dataProvider;

    public DocumentsOverview(@Autowired StorageService storageService, @Autowired AccessControl accessControl) {
        setSizeFull();

        dataProvider = new DocumentDataProvider(storageService, accessControl.getPrincipalName());

        DocumentGrid documentGrid = new DocumentGrid();
        documentPreview =
                new DocumentPreview(documentGrid, storageService.findTagsByOwner(accessControl.getPrincipalName()));
        documentPreview.setVisible(false);
        documentGrid.addSelectionListener(getItemClickEventComponentEventListener());
        documentGrid.setDataProvider(dataProvider);

        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.setPlaceholder(Constants.FILTER_TEXT);
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(documentGrid, documentPreview);
        horizontalLayout.setFlexGrow(1, documentGrid);
        horizontalLayout.setFlexGrow(0, documentPreview);
        horizontalLayout.setSizeFull();

        final VerticalLayout barAndGridLayout = new VerticalLayout();
        final HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setWidth("100%");
        Button searchButton = new Button("Search", VaadinIcon.SEARCH.create());
        searchButton.setThemeName("primary");
        searchButton.addClickListener(event -> dataProvider.setFilter(filter.getValue()));
        searchButton.setWidth("10%");
        searchLayout.add(filter);
        //searchLayout.expand(filter);
        searchLayout.add(searchButton);
        filter.setClearButtonVisible(true);

        barAndGridLayout.add(searchLayout);

        barAndGridLayout.add(horizontalLayout);
        add(barAndGridLayout);
    }

    private SelectionListener<Grid<Document>, Document> getItemClickEventComponentEventListener() {
        return event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                documentPreview.loadImage();
                documentPreview.loadProperties();
                documentPreview.setVisible(true);
                horizontalLayout.setFlexGrow(1, documentPreview);
            } else {
                documentPreview.setVisible(false);
                horizontalLayout.setFlexGrow(0, documentPreview);
            }
        };
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }
}
