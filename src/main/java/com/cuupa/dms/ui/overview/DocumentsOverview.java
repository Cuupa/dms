package com.cuupa.dms.ui.overview;

import com.cuupa.dms.Constants;
import com.cuupa.dms.authentication.AccessControlFactory;
import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.document.db.DocumentDataProvider;
import com.cuupa.dms.ui.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "Documents", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
public class DocumentsOverview extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Documents";

    private final DocumentGrid documentGrid;

    private final DocumentPreview documentPreview;

    private final HorizontalLayout horizontalLayout;

    private final DocumentDataProvider dataProvider;

    private TextField filter;

    public DocumentsOverview(@Autowired StorageService storageService) {
        setSizeFull();
        dataProvider =
                new DocumentDataProvider(storageService,
                                         AccessControlFactory.getInstance().createAccessControl().getPrincipalName());

        documentGrid = new DocumentGrid();
        documentPreview =
                new DocumentPreview(documentGrid,
                                    storageService.findTagsByOwner(AccessControlFactory.getInstance()
                                                                                       .createAccessControl()
                                                                                       .getPrincipalName()));
        documentPreview.setVisible(false);
        documentGrid.addSelectionListener(getItemClickEventComponentEventListener());
        documentGrid.setDataProvider(dataProvider);

        filter = new TextField();
        filter.setWidth("100%");
        filter.setPlaceholder(Constants.FILTER_TEXT);
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(documentGrid, documentPreview);
        horizontalLayout.setFlexGrow(1, documentGrid);
        horizontalLayout.setFlexGrow(0, documentPreview);
        horizontalLayout.setSizeFull();

        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(filter);
        barAndGridLayout.setFlexGrow(1, filter);
        barAndGridLayout.expand(filter);

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
        //viewLogic.enter(parameter);
    }
}
