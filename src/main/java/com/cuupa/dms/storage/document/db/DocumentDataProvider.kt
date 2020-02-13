package com.cuupa.dms.storage.document.db;

import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentDataProvider extends ListDataProvider<Document> {

    private String filtertext;

    public DocumentDataProvider(StorageService storageService, String username) {
        super(storageService.findDocumentsByOwner(username));
    }

    public void setFilter(final String filtertext) {
        Objects.requireNonNull(filtertext, "Must not be null");
        final String finalFiltertext = filtertext.trim().toLowerCase();
        if (Objects.equals(this.filtertext, finalFiltertext)) {
            return;
        }

        this.filtertext = finalFiltertext;
        setFilter(document -> passesFilter(document.getName(), filtertext) ||
                              passesFilter(document.getTags()
                                                   .stream()
                                                   .map(tag -> tag.getName().toLowerCase())
                                                   .collect(Collectors.toList()), filtertext) ||
                              passesFilter(document.getSender(), filtertext));
    }

    public void setFilter(final Set<String> filter) {
        if (filter != null) {

            setFilter(document -> passesFilter(document.getTags()
                                                       .stream()
                                                       .map(tag -> tag.getName().toLowerCase())
                                                       .collect(Collectors.toList()), filter));
        }
    }


    private boolean passesFilter(final List<String> tags, Set<String> filter) {
        return tags != null && !filter.isEmpty() && filter.stream().anyMatch(tags::contains);
    }

    private boolean passesFilter(final String name, final String filtertext) {
        return name != null && name.toLowerCase().contains(filtertext);
    }

    private boolean passesFilter(final List<String> values, final String filtertext) {
        return values.stream().anyMatch(value -> value.contains(filtertext));
    }
}
