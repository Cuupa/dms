package com.cuupa.dms.ui.overview

import com.cuupa.dms.service.LocalDateTimeFormatter
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.Route
import org.apache.commons.lang3.StringUtils
import java.util.stream.Collectors

@Push
@Route("documentPush")
class DocumentGrid : Grid<Document>() {

    private fun addContent() {
        addColumn(Document::name).setHeader("Filename").setFlexGrow(20).setSortable(true).key = "name"
        addColumn(Document::sender).setHeader("From").setFlexGrow(20).setSortable(true).key = "sender"
        addColumn { (_, _, _, _, createDate) -> dateFormatter.format(createDate) }.setHeader("Date")
                .setFlexGrow(20)
                .setSortable(true).key = "createDate"
        addColumn { document: Document -> getTaglist(document) }.setFlexGrow(20).setHeader("Tags").setSortable(true).key = "tags"
    }

    private fun getTaglist(document: Document): String {
        val tags = document.tags.stream().map(Tag::name).sorted().collect(Collectors.joining(", "))
        return if (StringUtils.isEmpty(tags)) {
            ""
        } else tags
    }

    override fun onAttach(attachEvent: AttachEvent) {
        super.onAttach(attachEvent)
        UI.getCurrent().internals.extendedClientDetails = null
    }

    companion object {
        private val dateFormatter = LocalDateTimeFormatter()
    }

    init {
        setSizeFull()
        addContent()
        //UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setCustomVisibility(e.getWidth()));
    }
}