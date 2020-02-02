package com.cuupa.dms.ui.overview;

import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;
import com.cuupa.dms.ui.LocalDateTimeFormatter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

@Push
@Route("push")
public class DocumentGrid extends Grid<Document> {

    private static final LocalDateTimeFormatter dateFormatter = new LocalDateTimeFormatter();

    public DocumentGrid() {
        setSizeFull();
        addContent();
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setCustomVisibility(e.getWidth()));
    }


    private void addContent() {
        addColumn(Document::getName).setHeader("Filename").setFlexGrow(20).setSortable(true).setKey("name");
        addColumn(Document::getSender).setHeader("From").setFlexGrow(20).setSortable(true).setKey("sender");
        addColumn(document -> dateFormatter.format(document.getCreateDate())).setHeader("Date")
                                                                             .setFlexGrow(20)
                                                                             .setSortable(true)
                                                                             .setKey("createDate");
        addColumn(this::getTaglist).setFlexGrow(20).setHeader("Tags").setSortable(true).setKey("tags");
    }

    private String getTaglist(Document document) {
        String tags = document.getTags().stream().map(Tag::getName).sorted().collect(Collectors.joining(", "));
        if (StringUtils.isEmpty(tags)) {
            return "";
        }

        return tags;
    }

    private void setCustomVisibility(int width) {
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> {
            setCustomVisibility(e.getBodyClientWidth());
        });
    }

}
