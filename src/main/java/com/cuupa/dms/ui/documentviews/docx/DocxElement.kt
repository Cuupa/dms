package com.cuupa.dms.ui.documentviews.docx

import com.vaadin.flow.component.HasSize
import com.vaadin.flow.component.Html
import com.vaadin.flow.dom.Element

class DocxElement(tag: String?) : Html(tag), HasSize {

    override fun getElement(): Element {
        return super.getElement()
    }

    companion object {
        private const val mediaType = "application/xhtml+xml"
    }

    init {
    }
}