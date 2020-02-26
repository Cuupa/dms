package com.cuupa.dms.ui.documentviews.pdf

import com.vaadin.flow.component.HasSize
import com.vaadin.flow.dom.Element

class PdfElement(tag: String?) : Element(tag), HasSize {

    override fun getElement(): Element {
        return this
    }

    companion object {
        private const val mediaType = "application/pdf"
    }

    init {
        setAttribute("type", mediaType)
        style["display"] = "block"
        setSizeFull()
    }
}