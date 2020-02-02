package com.cuupa.dms.ui.fileupload;

import com.cuupa.dms.service.extern.ExternSemanticService;
import com.cuupa.dms.service.extern.SemanticResult;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUploadSucceededListener implements ComponentEventListener<SucceededEvent> {

    private final ExternSemanticService externSemanticService;

    private final List<FileUploadProperties> properties;

    private final MultiFileMemoryBuffer buffer;

    public FileUploadSucceededListener(final ExternSemanticService externSemanticService, final List<FileUploadProperties> properties, final MultiFileMemoryBuffer buffer) {
        this.externSemanticService = externSemanticService;
        this.properties = properties;
        this.buffer = buffer;
    }

    @Override
    public void onComponentEvent(SucceededEvent event) {

        FileUploadProperties fileUploadProperties = new FileUploadProperties();

        fileUploadProperties.setFilename(event.getFileName());
        List<SemanticResult>
                semanticResults =
                getResultFromSemantic(externSemanticService, buffer.getInputStream(event.getFileName()));

        if (semanticResults.isEmpty()) {
            fileUploadProperties.setDate(LocalDate.now());
            fileUploadProperties.setTime(LocalTime.now());
        } else {
            fileUploadProperties.setFrom(semanticResults.get(0).getSender());
            fileUploadProperties.setDate(LocalDate.now());
            fileUploadProperties.setTime(LocalTime.now());
            List<String>
                    collect =
                    semanticResults.stream().map(SemanticResult::getTopicName).collect(Collectors.toList());
            //tags.setValue();
            fileUploadProperties.setTags(collect);
        }

        fileUploadProperties.setPdf(new StreamResource(event.getFileName(), () -> {
            try {
                return new ByteArrayInputStream((buffer.getInputStream(event.getFileName()).readAllBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }));

        fileUploadProperties.setContent(getBytes(buffer, event));

        properties.add(fileUploadProperties);
    }

    private byte[] getBytes(MultiFileMemoryBuffer buffer, SucceededEvent event) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.getInputStream(event.getFileName())
                                                                                        .readAllBytes())) {

            return byteArrayInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private List<SemanticResult> getResultFromSemantic(ExternSemanticService externSemanticService, InputStream inputStream) {
        try {
            byte[] value = IOUtils.toByteArray(inputStream);
            return externSemanticService.analize(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
