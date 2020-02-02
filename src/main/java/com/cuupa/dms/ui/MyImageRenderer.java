package com.cuupa.dms.ui;

import com.cuupa.dms.storage.document.Document;
import com.vaadin.flow.data.renderer.Renderer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyImageRenderer extends Renderer {

    private static final Executor executor = Executors.newSingleThreadExecutor();


    public void render(Document document) {
        Runnable runnable = () -> {
            try (final PDDocument pdDocument = PDDocument.load(new File(document.getFilename()))) {
                PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
                if (pdDocument.getNumberOfPages() > 0) {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
                    String fileName = document.getName().replace(".pdf", ".png");
                    ImageIOUtil.writeImage(bim, fileName, 300);
                }
            } catch (IOException e) {
                System.err.println("Exception while trying to create pdf document - " + e);
            }
        };

        //executor.execute(runnable);
    }
}
