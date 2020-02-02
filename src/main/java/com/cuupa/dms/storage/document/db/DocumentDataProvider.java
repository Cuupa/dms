package com.cuupa.dms.storage.document.db;

import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DocumentDataProvider extends ListDataProvider<Document> {

    private String filtertext;

    public DocumentDataProvider(StorageService storageService, String username) {
        super(storageService.findDocumentsByOwner(username));
    }

    @Override
    public Long getId(final Document document) {
        Objects.requireNonNull(document, "Cannot provide an id for a null product.");
        return document.getId();
    }

    public void setFilter(final String filtertext) {
        Objects.requireNonNull(filtertext, "Must not be null");
        if (Objects.equals(this.filtertext, filtertext.trim())) {
            return;
        }

        this.filtertext = filtertext.trim();
        setFilter(document -> passesFilter(document.getName(), filtertext) ||
                              passesFilter(document.getTags().stream().map(Tag::getName).collect(Collectors.toList()),
                                           filtertext) ||
                              passesFilter(document.getSender(), filtertext));
    }

    private boolean passesFilter(final String name, final String filtertext) {
        return name != null && name.toLowerCase().contains(filtertext);
    }

    private boolean passesFilter(final List<String> values, final String filtertext) {
        return values.stream().anyMatch(value -> value.contains(filtertext));
    }
}
