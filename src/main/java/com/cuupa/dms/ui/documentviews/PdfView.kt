package com.cuupa.dms.ui.documentviews

import com.cuupa.dms.storage.document.Document
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class PdfView : VerticalLayout {

    constructor(document: Document) {
        val streamResource = StreamResource(document.name, InputStreamFactory { getInputStream(document.filename) })
        init(streamResource)
    }

    constructor()
    constructor(streamResource: StreamResource) {
        init(streamResource)
    }

    private fun init(streamResource: StreamResource) {
        val view = PdfElement("object")
        view.setAttribute("data", streamResource)
        element.appendChild(view)
        setSizeFull()
    }

    private fun getInputStream(filename: String): InputStream? {
        try {
            FileInputStream(filename).use { inputStream -> return ByteArrayInputStream(inputStream.readAllBytes()) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}