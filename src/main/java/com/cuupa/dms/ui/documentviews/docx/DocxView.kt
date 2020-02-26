package com.cuupa.dms.ui.documentviews.docx

import com.cuupa.dms.storage.document.Document
import com.cuupa.dms.ui.documentviews.DocumentView
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.server.VaadinSession
import org.docx4j.Docx4J
import org.docx4j.Docx4jProperties
import org.docx4j.fonts.BestMatchingMapper
import org.docx4j.fonts.Mapper
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import java.io.*
import java.nio.charset.StandardCharsets

class DocxView : DocumentView {

    constructor(document: Document) {
        val streamResource = StreamResource(document.name.replace(".docx", ".xhtml"), InputStreamFactory {
            getInputStream(document.filename)
        })
        init(streamResource)
    }

    constructor()
    constructor(streamResource: StreamResource) {
        init(streamResource)
    }

    private fun init(streamResource: StreamResource) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        streamResource.writer.accept(byteArrayOutputStream, VaadinSession.getCurrent())
        val view = DocxElement(byteArrayOutputStream.toString(StandardCharsets.UTF_8))
        element.appendChild(view.element)
        setSizeFull()
    }

    private fun getInputStream(filename: String): InputStream? {
        try {

            FileInputStream(filename).use {
                val wordprocessingMLPackage: WordprocessingMLPackage = Docx4J.load(it)
                val htmlSettings = Docx4J.createHTMLSettings()
                htmlSettings.imageDirPath = filename + "_files"
                htmlSettings.imageTargetUri = filename.substring(filename.lastIndexOf("/") + 1) + "_files"
                htmlSettings.wmlPackage = wordprocessingMLPackage
                val fontMapper: Mapper = BestMatchingMapper() // better for Linux
                wordprocessingMLPackage.fontMapper = fontMapper
                Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true)
                val stream = ByteArrayOutputStream(DEFAULT_BUFFER_SIZE)
                Docx4J.toHTML(htmlSettings, stream, Docx4J.FLAG_EXPORT_PREFER_XSL)
                return ByteArrayInputStream(stream.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}