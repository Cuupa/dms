package com.cuupa.dms.ui.fileupload;

import com.cuupa.dms.Constants;
import com.cuupa.dms.authentication.AccessControlFactory;
import com.cuupa.dms.storage.StorageService;
import com.cuupa.dms.storage.document.Document;
import com.cuupa.dms.storage.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class DocumentSaveUtil {

    private final FileUploadProperties properties;

    private final StorageService storageService;

    protected DocumentSaveUtil(FileUploadProperties properties, StorageService storageService) {
        this.properties = properties;
        this.storageService = storageService;
    }

    public void save() {
        Path path = writeFile();
        if (path != null) {
            writeDatabase(path);
        }
    }

    private void writeDatabase(Path path) {
        storageService.save(new Document(0L,
                                         path.toString(),
                                         properties.getFilename(),
                                         properties.getFrom(),
                                         AccessControlFactory.getInstance().createAccessControl().getPrincipalName(),
                                         LocalDateTime.of(properties.getDate(), properties.getTime()),
                                         properties.getTags().stream().map(Tag::new).collect(Collectors.toList())));
    }

    private Path writeFile() {
        try {
            Path
                    path =
                    Paths.get(Constants.DOCUMENTFOLDER +
                              File.separator +
                              AccessControlFactory.getInstance().createAccessControl().getPrincipalName() +
                              File.separator +
                              properties.getFilename());
            path.getParent().toFile().mkdirs();
            if (!Files.exists(path)) {
                Files.write(path, properties.getContent(), StandardOpenOption.CREATE_NEW);
            }
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
