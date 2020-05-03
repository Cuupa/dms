package com.cuupa.dms.ui.inbox

import com.cuupa.dms.UIConstants
import com.cuupa.dms.service.LocalDateTimeFormatter
import com.cuupa.dms.storage.StorageService
import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.storage.tag.Tag
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.Route
import org.apache.commons.lang3.StringUtils
import java.util.stream.Collectors

@Push
@Route("inboxPush")
class InboxGrid(val storageService: StorageService) : Grid<Document>() {

    init {
        setSizeFull()
        addContent()
        //UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setCustomVisibility(e.getWidth()));
    }

    private fun addContent() {
        addComponentColumn { source: Document -> getIconToDisplay(source) }.setHeader("Open Task")
                .setFlexGrow(10).key = "open task"
        addColumn(Document::name).setHeader(getTranslation(UIConstants.filename)).setFlexGrow(20).setSortable(true)
                .key = "name"
        addColumn(Document::sender).setHeader(getTranslation(UIConstants.from)).setFlexGrow(20).setSortable(true).key =
                "sender"
        addColumn { (_, _, _, _, createDate) -> dateFormatter.format(createDate) }.setHeader(getTranslation
        (UIConstants.dateOfReciept))
                .setFlexGrow(20)
                .setSortable(true).key = "createDate"
        addColumn { document: Document -> getTaglist(document) }.setFlexGrow(20).setHeader(getTranslation(UIConstants
                .tags))
                .setSortable(true).key = "tags"
    }

    fun getIconToDisplay(document: Document): Icon {
        return if (document.processInstanceId.isNullOrBlank()) {
            VaadinIcon.ARCHIVE.create()
        } else {
            VaadinIcon.PAPERCLIP.create()
        }
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
}