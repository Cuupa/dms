package com.cuupa.dms.ui.documentviews

import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.ui.documentviews.docx.DocxView
import com.cuupa.dms.ui.documentviews.pdf.PdfView
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.server.StreamResource

open class DocumentView : VerticalLayout() {

    fun get(document: Document): DocumentView {
        if (document.filename.endsWith(".pdf")) {
            return PdfView(document)
        } else if (document.filename.endsWith(".docx")) {
            return DocxView(document)
        }

        return DocumentView()
    }

    fun get(filename: String, streamResource: StreamResource): DocumentView {
        if (filename.endsWith(".pdf")) {
            return PdfView(streamResource)
        } else if (filename.endsWith(".docx")) {
            return DocxView(streamResource)
        }

        return DocumentView()
    }
}
